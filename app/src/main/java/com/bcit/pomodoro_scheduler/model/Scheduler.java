package com.bcit.pomodoro_scheduler.model;

import com.google.firebase.Timestamp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Scheduler {
    private HashMap<LocalDate, ArrayList<Task>> schedule;

    private HashMap<DayOfWeek, ArrayList<Task>> repeatCommitments;
    private HashMap<LocalDate, ArrayList<Task>> singleCommitments;

    private ArrayList<GoalDataForScheduler> goals;

    private ArrayList<GoalDataForScheduler> high_priority;
    private ArrayList<GoalDataForScheduler> med_priority;
    private ArrayList<GoalDataForScheduler> low_priority;

    private int startMinutes;
    private int endMinutes;
    private int currentMinutes;

    private final int workBlockL = 50;

    private int interleaveIndex = 0;
    private int maxInterleaves;

    public Scheduler() {
        this.repeatCommitments = getStudentCommitments();
        this.singleCommitments = new HashMap<>();
        this.startMinutes = 420;
        this.endMinutes = 1200;
        this.maxInterleaves = 1;
        this.goals = getStudentGoals();

        high_priority = new ArrayList<>();
        med_priority = new ArrayList<>();
        low_priority = new ArrayList<>();

        this.schedule = new HashMap<>();
        createSchedule();
    }

    public Scheduler(HashMap<DayOfWeek, ArrayList<Task>> repeatCommitments,
                     HashMap<LocalDate, ArrayList<Task>> singleCommitments,
                     ArrayList<GoalDataForScheduler> goals,
                     int startMinutes, int endMinutes, int maxInterleaves) {
        this.repeatCommitments = repeatCommitments;
        this.singleCommitments = singleCommitments;
        this.startMinutes = startMinutes;
        this.endMinutes = endMinutes;
        this.maxInterleaves = maxInterleaves - 1;

        this.goals = goals;
        high_priority = new ArrayList<>();
        med_priority = new ArrayList<>();
        low_priority = new ArrayList<>();

        this.schedule = new HashMap<>();
        createSchedule();
    }

    public Scheduler(HashMap<Repeat, List<Commitment>> commitmentHashMap, List<Goal> goals) {
        this.repeatCommitments = getRepeatCommitments(commitmentHashMap);
        this.singleCommitments = getSingleCommitments(
                Objects.requireNonNull(commitmentHashMap.get(Repeat.NEVER))
        );
        this.goals = getGoalDataForScheduler(goals);

        high_priority = new ArrayList<>();
        med_priority = new ArrayList<>();
        low_priority = new ArrayList<>();

        this.schedule = new HashMap<>();

        this.startMinutes = 420;
        this.endMinutes = 1200;
        this.maxInterleaves = 1;

        createSchedule();
    }

    private ArrayList<GoalDataForScheduler> getGoalDataForScheduler(List<Goal> goals) {
        ArrayList<GoalDataForScheduler> goalDataForSchedulers = new ArrayList<>();
        for (Goal goal : goals) {
            int days = getDaysBetween(goal.getDeadline().toDate(), new Date());
            GoalDataForScheduler goalData = new GoalDataForScheduler(
                    goal.getId(),
                    goal.getName(),
                    goal.getPriority(),
                    days,
                    goal.getTotalTimeInMinutes(),
                    workBlockL
            );
            goalDataForSchedulers.add(goalData);
        }
        return goalDataForSchedulers;
    }

    private void setGoals(ArrayList<GoalDataForScheduler> goals) {
        Collections.sort(goals, Collections.reverseOrder());
        separatePriorities(goals);
    }

    private void separatePriorities(ArrayList<GoalDataForScheduler> goals) {
        for (GoalDataForScheduler goal : goals) {
            if (goal.getTask_priority() == Priority.HIGH) {
                high_priority.add(goal);
            }
            if (goal.getTask_priority() == Priority.MEDIUM) {
                med_priority.add(goal);
            }
            if (goal.getTask_priority() == Priority.LOW) {
                low_priority.add(goal);
            }
        }
    }

    private boolean goalsLeft() {
        return (high_priority.size() + med_priority.size() + low_priority.size()) > 0;
    }

    public HashMap<LocalDate, ArrayList<Task>> getSchedule() {
        return schedule;
    }

    private void createSchedule() {
        setGoals(goals);
        LocalDate startDate = LocalDate.now();
        while (goalsLeft()) {
            createScheduleToday(startDate);
            startDate = startDate.plusDays(1);
        }
    }

    private void createScheduleToday(LocalDate today) {
        ArrayList<Task> scheduleToday = new ArrayList<>();

        ArrayList<Task> allCommitmentsToday = repeatCommitments.get(today.getDayOfWeek());
        if (singleCommitments.get(today) != null)
            allCommitmentsToday.addAll(singleCommitments.get(today));
        Collections.sort(allCommitmentsToday);

        currentMinutes = startMinutes;
        if (allCommitmentsToday.size() > 0) {
            addGoalWorkBlocks(scheduleToday, allCommitmentsToday.get(0).getStartTime() - startMinutes);
            for (int i = 0; i < allCommitmentsToday.size() - 1; i++) {
                scheduleToday.add(allCommitmentsToday.get(i));
                currentMinutes = allCommitmentsToday.get(i).getEndTime();
                int workBlockMinutes = allCommitmentsToday.get(i + 1).getStartTime() - allCommitmentsToday.get(i).getEndTime();
                addGoalWorkBlocks(scheduleToday, workBlockMinutes);
            }
            scheduleToday.add(allCommitmentsToday.get(allCommitmentsToday.size() - 1));
            currentMinutes = allCommitmentsToday.get(allCommitmentsToday.size() - 1).getEndTime();
            int workBlockMinutes = endMinutes - currentMinutes;
            addGoalWorkBlocks(scheduleToday, workBlockMinutes);
        } else {
            addGoalWorkBlocks(scheduleToday, endMinutes - currentMinutes);
        }
        schedule.put(today, scheduleToday);
    }

    private void addGoalWorkBlocks(ArrayList<Task> scheduleToday, int workBlockMinutes) {
        boolean longRest = false;
        int workBlockSize = workBlockL + 10;
        while (workBlockMinutes > 0 && goalsLeft()) {
            if (workBlockMinutes >= workBlockSize) {
                Task task = getGoalWorkBlock(workBlockL, currentMinutes);
                if (task.getDaysLeft() > 0 && task.getEndTime() - task.getStartTime() >= 10) {
                    int restSize = workBlockSize - workBlockL;
                    Task rest = new Task("rest", "Break " + restSize, task.getEndTime(),
                            task.getEndTime() + restSize, 1, TaskType.BREAK);
                    scheduleToday.add(task);
                    scheduleToday.add(rest);
                    currentMinutes += workBlockSize;
                    workBlockMinutes -= workBlockSize;
                    longRest = longRest == false ? true : false;
                    workBlockSize = longRest ? workBlockL + 20 : workBlockL + 10;
                }
            } else {
                Task task = getGoalWorkBlock(workBlockMinutes, currentMinutes);
                if (task.getDaysLeft() > 0 && task.getEndTime() - task.getStartTime() >= 10) {
                    scheduleToday.add(task);
                    currentMinutes += workBlockMinutes;
                    workBlockMinutes = 0;
                }
            }
        }
    }

    private Task getGoalWorkBlock(int minutes, int startMinutes) {
        ArrayList<GoalDataForScheduler> dataList = getPriorityListInPriorityOrder();
        GoalDataForScheduler data = dataList.get(interleaveIndex);
        int taskTime = data.takeMinutes(minutes);
        Task goalWorkBlock = new Task(data.getID(), data.getName(), startMinutes,
                startMinutes + taskTime, data.getDays_left(), TaskType.GOAL);
        if (data.getMinutes() < workBlockL / 3) {
            dataList.remove(interleaveIndex);
            interleaveIndex = interleaveIndex > dataList.size() - 1 ? 0 : interleaveIndex;
        } else {
            interleaveIndex = (interleaveIndex + 1 >= dataList.size() - 1 || interleaveIndex + 1 > maxInterleaves)
                    ? 0 : interleaveIndex + 1;
        }
        return goalWorkBlock;
    }

    private ArrayList<GoalDataForScheduler> getPriorityListInPriorityOrder() {
        if (high_priority.size() > 0) {
            return high_priority;
        }
        if (med_priority.size() > 0) {
            return med_priority;
        }
        return low_priority;
    }

    private int getDaysBetween(Date date1, Date date2) {
        LocalDate dateBefore = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateAfter = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(dateAfter, dateBefore);
    }

    private HashMap<LocalDate, ArrayList<Task>> getSingleCommitments(List<Commitment> commitments) {
        HashMap<LocalDate, ArrayList<Task>> singleCommitmentHashMap = new HashMap<>();
        for (Commitment commitment : commitments) {
            Date date = commitment.getStartTime().toDate();
            LocalDate key = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            singleCommitmentHashMap.putIfAbsent(key, new ArrayList<>());
            Objects.requireNonNull(singleCommitmentHashMap.get(key))
                    .add(getTaskFromCommitment(commitment));
        }
        return singleCommitmentHashMap;
    }

    private HashMap<DayOfWeek, ArrayList<Task>> getRepeatCommitments(
            HashMap<Repeat, List<Commitment>> commitmentHashMap) {
        HashMap<DayOfWeek, ArrayList<Task>> repeatCommitmentHashMap = new HashMap<>();
        for (Map.Entry<Repeat, List<Commitment>> entry : commitmentHashMap.entrySet()) {
            if (entry.getKey() == Repeat.NEVER) {
                continue;
            }

            DayOfWeek key = DayOfWeek.valueOf(entry.getKey().toString().toUpperCase());
            repeatCommitmentHashMap.put(key, new ArrayList<>());

            for (Commitment commitment : entry.getValue()) {
                Task task = getTaskFromCommitment(commitment);
                Objects.requireNonNull(repeatCommitmentHashMap.get(key)).add(task);
            }
        }
        return repeatCommitmentHashMap;
    }

    private Task getTaskFromCommitment(Commitment commitment) {
        Date startTimeDate = commitment.getStartTime().toDate();
        Date endTimeDate = commitment.getEndTime().toDate();
        LocalDateTime startLocalDateTime = startTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endLocalDateTime = endTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        int startTime = startLocalDateTime.getHour() * 60 + startLocalDateTime.getMinute();
        int endTime = endLocalDateTime.getHour() * 60 + endLocalDateTime.getMinute();
        return new Task(
                commitment.getId(),
                commitment.getName(),
                startTime,
                endTime,
                1,
                TaskType.COMMITMENT
        );
    }


    private HashMap<DayOfWeek, ArrayList<Task>> getStudentCommitments() {
        HashMap<DayOfWeek, ArrayList<Task>> schedule = new HashMap<>();
        ArrayList m = new ArrayList();
        ArrayList t = new ArrayList();
        ArrayList w = new ArrayList();
        ArrayList t2 = new ArrayList();
        ArrayList f = new ArrayList();
        ArrayList s = new ArrayList();
        ArrayList s2 = new ArrayList();

        Task a = new Task("ID", "OOP 2 Lec", 510, 600, 0, TaskType.COMMITMENT);
        Task a1 = new Task("ID", "Android Lab", 630, 720, 0, TaskType.COMMITMENT);
        Task a2 = new Task("ID", "Math Lab", 570, 680, 0, TaskType.COMMITMENT);
        Task a3 = new Task("ID", "OOP 2 Lab", 690, 780, 0, TaskType.COMMITMENT);
        Task a4 = new Task("ID", "Android Lec", 510, 600, 0, TaskType.COMMITMENT);
        Task a5 = new Task("ID", "Pred A Lec", 630, 720, 0, TaskType.COMMITMENT);
        Task a6 = new Task("ID", "Pred A Lab", 810, 960, 0, TaskType.COMMITMENT);
        Task a7 = new Task("ID", "OOP 2 Lec", 510, 600, 0, TaskType.COMMITMENT);
        Task a8 = new Task("ID", "Math Lec", 870, 960, 0, TaskType.COMMITMENT);
        Task b = new Task("ID", "Eat Dinner", 1080, 1140, 0, TaskType.COMMITMENT);
        Task c = new Task("ID", "Morning Prep", 420, 510, 0, TaskType.COMMITMENT);

        m.add(a);
        t.add(a1);
        w.add(a2);
        w.add(a3);
        t2.add(a4);
        t2.add(a5);
        t2.add(a6);
        f.add(a7);
        f.add(a8);

        m.add(b);
        t.add(b);
        w.add(b);
        t2.add(b);
        f.add(b);
        s.add(b);
        s2.add(b);

        m.add(c);
        t.add(c);
        w.add(c);
        t2.add(c);
        f.add(c);


        s.add(new Task("ID", "League of Legends", 420, 900, 0, TaskType.COMMITMENT));
        s2.add(new Task("ID", "League of Legends", 420, 900, 0, TaskType.COMMITMENT));

        schedule.put(DayOfWeek.MONDAY, m);
        schedule.put(DayOfWeek.TUESDAY, t);
        schedule.put(DayOfWeek.WEDNESDAY, w);
        schedule.put(DayOfWeek.THURSDAY, t2);
        schedule.put(DayOfWeek.FRIDAY, f);
        schedule.put(DayOfWeek.SATURDAY, s);
        schedule.put(DayOfWeek.SUNDAY, s2);

        return schedule;
    }

    private ArrayList<GoalDataForScheduler> getStudentGoals() {
        ArrayList<GoalDataForScheduler> goals = new ArrayList<>();
        goals.add(new GoalDataForScheduler("ID", "OOP 2 Lab 8", Priority.HIGH, 1, 120, 50));
        goals.add(new GoalDataForScheduler("ID", "Study Math Quiz", Priority.HIGH, 1, 120, 50));
        goals.add(new GoalDataForScheduler("ID", "Android Project", Priority.HIGH, 5, 360, 50));
        goals.add(new GoalDataForScheduler("ID", "OOP 2 Assigment 3", Priority.HIGH, 10, 420, 50));
        goals.add(new GoalDataForScheduler("ID", "Pred Assignment", Priority.HIGH, 10, 420, 50));
        goals.add(new GoalDataForScheduler("ID", "Pred Lec 10", Priority.HIGH, 2, 120, 50));
        goals.add(new GoalDataForScheduler("ID", "Pred Lab 10", Priority.HIGH, 2, 120, 50));
        goals.add(new GoalDataForScheduler("ID", "Study Math Final", Priority.HIGH, 13, 600, 50));
        goals.add(new GoalDataForScheduler("ID", "Study Android Final", Priority.HIGH, 12, 240, 50));
        goals.add(new GoalDataForScheduler("ID", "Study OOP Final", Priority.HIGH, 13, 720, 50));
        goals.add(new GoalDataForScheduler("ID", "Study Pred Final", Priority.HIGH, 16, 600, 50));
        return goals;
    }

    public static void main(String[] args) {
        Scheduler s = new Scheduler();
        HashMap<LocalDate, ArrayList<Task>> sched = s.getSchedule();
        for (LocalDate date : sched.keySet()) {
            System.out.println("DATE------" + date.toString());
            ArrayList<Task> tasks = sched.get(date);
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }
}

