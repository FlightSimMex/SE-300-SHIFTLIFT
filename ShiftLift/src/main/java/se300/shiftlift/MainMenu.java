package se300.shiftlift;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private final List<StudentWorker> workers = new ArrayList<>();
    private final List<Workstation> workstations = new ArrayList<>();
    private final List<Schedule> schedules = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new MainMenu().run();
    }

    public void run() {
        while (true) {
            printMainOptions();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> manageWorkers();
                case "2" -> manageWorkstations();
                case "3" -> manageSchedules();
                case "q", "Q" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void printMainOptions() {
        System.out.println("--- Main Menu ---");
        System.out.println("1) Manage Student Workers");
        System.out.println("2) Manage Workstations");
        System.out.println("3) Manage Schedules");
        System.out.println("Q) Quit");
        System.out.print("> ");
    }

    private void manageWorkers() {
        while (true) {
            System.out.println("--- Student Workers ---");
            System.out.println("1) List");
            System.out.println("2) Add");
            System.out.println("3) Remove");
            System.out.println("4) Edit");
            System.out.println("B) Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> listWorkers();
                case "2" -> addWorker();
                case "3" -> removeWorker();
                case "4" -> editWorker();
                case "B", "b" -> { return; }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void listWorkers() {
        if (workers.isEmpty()) { System.out.println("No workers"); return; }
        for (int i = 0; i < workers.size(); i++) {
            System.out.println(i + ") " + workers.get(i));
        }
    }

    private void addWorker() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        try {
            StudentWorker w = new StudentWorker(email, password);
            workers.add(w);
            System.out.println("Added: " + w.getUsername());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add worker: " + e.getMessage());
        }
    }

    private void removeWorker() {
        listWorkers();
        System.out.print("Index to remove: ");
        String s = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(s);
            if (idx >= 0 && idx < workers.size()) {
                StudentWorker removed = workers.remove(idx);
                System.out.println("Removed: " + removed.getUsername());
            } else System.out.println("Index out of range");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void editWorker() {
        listWorkers();
        System.out.print("Index to edit: ");
        String s = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(s);
            if (idx >= 0 && idx < workers.size()) {
                StudentWorker w = workers.get(idx);
                System.out.println("Editing " + w.getUsername());
                System.out.print("New email (blank to keep): ");
                String email = scanner.nextLine().trim();
                if (!email.isEmpty()) w.setEmail(email);
                System.out.print("New password (blank to keep): ");
                String pwd = scanner.nextLine().trim();
                if (!pwd.isEmpty()) w.setPassword(pwd);
                System.out.print("New scheduled hours (blank to keep): ");
                String hrs = scanner.nextLine().trim();
                if (!hrs.isEmpty()) {
                    try { w.setScheduled_hours(Integer.parseInt(hrs)); } catch (Exception ex) { System.out.println("Failed to set hours: " + ex.getMessage()); }
                }
                System.out.println("Updated: " + w);
            } else System.out.println("Index out of range");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void manageWorkstations() {
        while (true) {
            System.out.println("--- Workstations ---");
            System.out.println("1) List");
            System.out.println("2) Add");
            System.out.println("3) Remove");
            System.out.println("B) Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> listWorkstations();
                case "2" -> addWorkstation();
                case "3" -> removeWorkstation();
                case "B", "b" -> { return; }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void listWorkstations() {
        if (workstations.isEmpty()) { System.out.println("No workstations"); return; }
        for (int i = 0; i < workstations.size(); i++) System.out.println(i + ") " + workstations.get(i).getName());
    }

    private void addWorkstation() {
        System.out.print("Workstation name: ");
        String name = scanner.nextLine().trim();
        try {
            Workstation ws = new Workstation(name);
            workstations.add(ws);
            System.out.println("Added: " + name);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add workstation: " + e.getMessage());
        }
    }

    private void removeWorkstation() {
        listWorkstations();
        System.out.print("Index to remove: ");
        String s = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(s);
            if (idx >= 0 && idx < workstations.size()) {
                Workstation removed = workstations.remove(idx);
                System.out.println("Removed: " + removed.getName());
            } else System.out.println("Index out of range");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void manageSchedules() {
        while (true) {
            System.out.println("--- Schedules ---");
            System.out.println("1) List");
            System.out.println("2) Create");
            System.out.println("3) Select to modify");
            System.out.println("B) Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> listSchedules();
                case "2" -> createSchedule();
                case "3" -> selectScheduleToModify();
                case "B", "b" -> { return; }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void listSchedules() {
        if (schedules.isEmpty()) { System.out.println("No schedules"); return; }
        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = schedules.get(i);
            System.out.println(i + ") " + s.getSchedule_start_date() + " - " + s.getSchedule_end_date() + " approved=" + s.getIs_approved());
        }
    }

    private void createSchedule() {
        System.out.print("Start date (m/d/yyyy) e.g. 1/1/2025: ");
        String start = scanner.nextLine().trim();
        System.out.print("End date (m/d/yyyy) e.g. 1/7/2025: ");
        String end = scanner.nextLine().trim();
        try {
            Date sdate = parseDate(start);
            Date edate = parseDate(end);
            Schedule schedule = new Schedule(sdate, edate);
            schedules.add(schedule);
            System.out.println("Created schedule: " + sdate + " - " + edate);
        } catch (Exception e) {
            System.out.println("Failed to create schedule: " + e.getMessage());
        }
    }

    private Date parseDate(String s) {
        String[] parts = s.split("/");
        if (parts.length != 3) throw new IllegalArgumentException("Date must be m/d/yyyy");
        int m = Integer.parseInt(parts[0]);
        int d = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        return new Date(d, m, y);
    }

    private void selectScheduleToModify() {
        listSchedules();
        System.out.print("Index to modify: ");
        String s = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(s);
            if (idx >= 0 && idx < schedules.size()) {
                modifySchedule(schedules.get(idx));
            } else System.out.println("Index out of range");
        } catch (NumberFormatException e) { System.out.println("Invalid number"); }
    }

    private void modifySchedule(Schedule schedule) {
        while (true) {
            System.out.println("--- Modify Schedule " + schedule.getSchedule_start_date() + " - " + schedule.getSchedule_end_date() + " ---");
            System.out.println("1) List shifts");
            System.out.println("2) Add shift");
            System.out.println("3) Remove shift");
            System.out.println("4) Find and edit shift");
            System.out.println("5) Approve schedule");
            System.out.println("B) Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> listShifts(schedule);
                case "2" -> addShiftToSchedule(schedule);
                case "3" -> removeShiftFromSchedule(schedule);
                case "4" -> findAndEditShift(schedule);
                case "5" -> { schedule.approve_schedule(); System.out.println("Schedule approved"); }
                case "B", "b" -> { return; }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void listShifts(Schedule schedule) {
        List<Shift> shifts = schedule.getShifts();
        if (shifts.isEmpty()) { System.out.println("No shifts for this schedule."); return; }
        for (int i = 0; i < shifts.size(); i++) {
            Shift sh = shifts.get(i);
            System.out.println(i + ") " + sh.getDate() + " " + sh.getTime().getStart_time() + "-" + sh.getTime().getEnd_time() + " | workstation=" + sh.getWorkstation().getName() + " | worker=" + sh.getStudentWorker().getUsername());
        }
    }

    private void addShiftToSchedule(Schedule schedule) {
        try {
            System.out.print("Date (m/d/yyyy): ");
            Date date = parseDate(scanner.nextLine().trim());
            System.out.print("Start time (e.g. 800): ");
            int start = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("End time (e.g. 1200): ");
            int end = Integer.parseInt(scanner.nextLine().trim());
            Time time = new Time(start, end);
            System.out.println("Select workstation by index:");
            listWorkstations();
            int widx = Integer.parseInt(scanner.nextLine().trim());
            Workstation ws = workstations.get(widx);
            System.out.println("Select worker by index:");
            listWorkers();
            int pidx = Integer.parseInt(scanner.nextLine().trim());
            StudentWorker sw = workers.get(pidx);
            schedule.addShift(date, time, ws, sw);
            System.out.println("Shift added");
        } catch (Exception e) {
            System.out.println("Failed to add shift: " + e.getMessage());
        }
    }

    private void removeShiftFromSchedule(Schedule schedule) {
        try {
            System.out.print("Date (m/d/yyyy): ");
            Date date = parseDate(scanner.nextLine().trim());
            System.out.print("Start time (e.g. 800): ");
            int start = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Select worker by index:");
            listWorkers();
            int pidx = Integer.parseInt(scanner.nextLine().trim());
            StudentWorker sw = workers.get(pidx);
            Shift removed = schedule.removeShift(date, start, sw);
            if (removed != null) System.out.println("Removed shift"); else System.out.println("No matching shift found");
        } catch (Exception e) { System.out.println("Failed to remove shift: " + e.getMessage()); }
    }

    private void findAndEditShift(Schedule schedule) {
        try {
            System.out.print("Date (m/d/yyyy): ");
            Date date = parseDate(scanner.nextLine().trim());
            System.out.print("Start time (e.g. 800): ");
            int start = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Select worker by index:");
            listWorkers();
            int pidx = Integer.parseInt(scanner.nextLine().trim());
            StudentWorker sw = workers.get(pidx);
            Shift found = schedule.findShift(date, start, sw);
            if (found == null) { System.out.println("Shift not found"); return; }
            System.out.println("Found shift. Edit options:");
            System.out.println("1) Change workstation");
            System.out.println("2) Change time");
            System.out.println("3) Change date");
            System.out.println("4) Change worker");
            System.out.println("B) Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> {
                    listWorkstations();
                    int widx = Integer.parseInt(scanner.nextLine().trim());
                    found.changeWorkstation(workstations.get(widx));
                    System.out.println("Workstation changed");
                }
                case "2" -> {
                    System.out.print("New start (e.g. 900): ");
                    int ns = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("New end (e.g. 1300): ");
                    int ne = Integer.parseInt(scanner.nextLine().trim());
                    found.changeTime(new Time(ns, ne));
                    System.out.println("Time changed");
                }
                case "3" -> {
                    System.out.print("New date (m/d/yyyy): ");
                    Date nd = parseDate(scanner.nextLine().trim());
                    found.changeDate(nd);
                    System.out.println("Date changed");
                }
                case "4" -> {
                    System.out.println("Select new worker by index:");
                    listWorkers();
                    int newIdx = Integer.parseInt(scanner.nextLine().trim());
                    if (newIdx >= 0 && newIdx < workers.size()) {
                        found.changeStudentWorker(workers.get(newIdx));
                        System.out.println("Worker changed");
                    } else {
                        System.out.println("Index out of range");
                    }
                }
                default -> System.out.println("Unknown option or cancelled");
            }
        } catch (Exception e) { System.out.println("Failed to edit shift: " + e.getMessage()); }
    }

}
