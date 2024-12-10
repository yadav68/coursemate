package com.coursemate;

import com.coursemate.dao.StudentDAO;
import com.coursemate.dao.TeacherDAO;
import com.coursemate.dao.UserDAO;
import com.coursemate.models.Course;
import com.coursemate.models.Student;
import com.coursemate.models.Teacher;
import com.coursemate.models.User;
import com.coursemate.services.AdminActions;
import com.coursemate.services.StudentActions;
import com.coursemate.services.TeacherActions;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final UserDAO userDAO = new UserDAO();
    private static final TeacherDAO teacherDAO = new TeacherDAO();
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final AdminActions adminActions = new AdminActions();
    private static final TeacherActions teacherActions = new TeacherActions();
    private static final StudentActions studentActions = new StudentActions();


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Welcome to CourseMate!");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = userDAO.authenticateUser(username, password);
            if (user == null) {
                System.out.println("Invalid username or password.");
                return;
            }

            System.out.println("Login successful. Role: " + user.getRole());
            switch (user.getRole()) {
                case "admin":
                    adminMenu(scanner);
                    break;
                case "teacher":
                    try {
                        // Fetch TeacherID using UserID
                        int teacherId = teacherDAO.getTeacherIdByUserId(user.getUserId());
                        teacherMenu(scanner, teacherId);
                    } catch (SQLException e) {
                        System.err.println("Error fetching TeacherID: " + e.getMessage());
                    }
                    break;
                case "student":
                    try {
                        // Fetch StudentID using UserID
                        int studentId = studentDAO.getStudentIdByUserId(user.getUserId());
                        studentMenu(scanner, studentId);
                    } catch (SQLException e) {
                        System.err.println("Error fetching StudentID: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid role.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

//    private static void adminMenu(Scanner scanner) throws SQLException {
//        while (true) {
//            System.out.println("\n--- Admin Menu ---");
//            System.out.println("1. Add Course");
//            System.out.println("2. View All Courses");
//            System.out.println("3. Update a Course");
//            System.out.println("4. Delete a Course");
//            System.out.println("5. View All Teachers");
//            System.out.println("6. Update a Teacher");
//            System.out.println("7. Delete a Teacher");
//            System.out.println("8. View All Students");
//            System.out.println("9. Update a Student");
//            System.out.println("10. Delete a Student");
//            System.out.println("11. Logout");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (choice) {
//                case 1:
//                    System.out.print("Enter Course Name: ");
//                    String courseName = scanner.nextLine();
//                    System.out.print("Enter Course Description: ");
//                    String courseDescription = scanner.nextLine();
//                    System.out.print("Enter Course Credits: ");
//                    int credits = scanner.nextInt();
//                    scanner.nextLine();
//                    adminActions.addCourse(new Course(0, courseName, courseDescription, credits));
//                    System.out.println("Course added successfully.");
//                    break;
//
//                case 2:
//                    for (Course course : adminActions.viewAllCourses()) {
//                        System.out.println(course);
//                    }
//                    break;
//
//                case 3:
//                    System.out.print("Enter Course ID to update: ");
//                    int courseIdToUpdate = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Enter New Course Name: ");
//                    String newCourseName = scanner.nextLine();
//                    System.out.print("Enter New Description: ");
//                    String newDescription = scanner.nextLine();
//                    System.out.print("Enter New Credits: ");
//                    int newCredits = scanner.nextInt();
//                    scanner.nextLine();
//                    adminActions.updateCourse(new Course(courseIdToUpdate, newCourseName, newDescription, newCredits));
//                    System.out.println("Course updated successfully.");
//                    break;
//
//                case 4:
//                    System.out.print("Enter Course ID to delete: ");
//                    int courseIdToDelete = scanner.nextInt();
//                    scanner.nextLine();
//                    adminActions.deleteCourse(courseIdToDelete);
//                    System.out.println("Course deleted successfully.");
//                    break;
//
//                case 5:
//                    for (var teacher : adminActions.viewAllTeachers()) {
//                        System.out.println(teacher);
//                    }
//                    break;
//
//                case 6:
//                    System.out.print("Enter Teacher ID to update: ");
//                    int teacherIdToUpdate = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Enter New Teacher Name: ");
//                    String newTeacherName = scanner.nextLine();
//                    System.out.print("Enter New Specialization: ");
//                    String newSpecialization = scanner.nextLine();
//                    adminActions.updateTeacher(new Teacher(teacherIdToUpdate, 0, newTeacherName, newSpecialization));
//                    System.out.println("Teacher updated successfully.");
//                    break;
//
//                case 7:
//                    System.out.print("Enter Teacher ID to delete: ");
//                    int teacherIdToDelete = scanner.nextInt();
//                    scanner.nextLine();
//                    adminActions.deleteTeacher(teacherIdToDelete);
//                    System.out.println("Teacher deleted successfully.");
//                    break;
//
//                case 8:
//                    for (var student : adminActions.viewAllStudents()) {
//                        System.out.println(student);
//                    }
//                    break;
//
//                case 9:
//                    System.out.print("Enter Student ID to update: ");
//                    int studentIdToUpdate = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Enter New Student Name: ");
//                    String newStudentName = scanner.nextLine();
//                    adminActions.updateStudent(new Student(studentIdToUpdate, 0, newStudentName));
//                    System.out.println("Student updated successfully.");
//                    break;
//
//                case 10:
//                    System.out.print("Enter Student ID to delete: ");
//                    int studentIdToDelete = scanner.nextInt();
//                    scanner.nextLine();
//                    adminActions.deleteStudent(studentIdToDelete);
//                    System.out.println("Student deleted successfully.");
//                    break;
//
//                case 11:
//                    System.out.println("Logging out...");
//                    return;
//
//                default:
//                    System.out.println("Invalid option.");
//            }
//        }
//    }

    private static void adminMenu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Update a Course");
            System.out.println("4. Delete a Course");
            System.out.println("5. View All Teachers");
            System.out.println("6. Update a Teacher");
            System.out.println("7. Delete a Teacher");
            System.out.println("8. View All Students");
            System.out.println("9. Update a Student");
            System.out.println("10. Delete a Student");
            System.out.println("11. Add Teacher");  // New option for adding a teacher
            System.out.println("12. Add Student");  // New option for adding a student
            System.out.println("13. Assign Course to Teacher");  // New option for assigning a course
            System.out.println("13. Assign Course to Student");
            System.out.println("15. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Course Name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter Course Description: ");
                    String courseDescription = scanner.nextLine();
                    System.out.print("Enter Course Credits: ");
                    int credits = scanner.nextInt();
                    scanner.nextLine();
                    adminActions.addCourse(new Course(0, courseName, courseDescription, credits));
                    System.out.println("Course added successfully.");
                    break;

                case 2:
                    for (Course course : adminActions.viewAllCourses()) {
                        System.out.println(course);
                    }
                    break;

                case 3:
                    System.out.print("Enter Course ID to update: ");
                    int courseIdToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Course Name: ");
                    String newCourseName = scanner.nextLine();
                    System.out.print("Enter New Description: ");
                    String newDescription = scanner.nextLine();
                    System.out.print("Enter New Credits: ");
                    int newCredits = scanner.nextInt();
                    scanner.nextLine();
                    adminActions.updateCourse(new Course(courseIdToUpdate, newCourseName, newDescription, newCredits));
                    System.out.println("Course updated successfully.");
                    break;

                case 4:
                    System.out.print("Enter Course ID to delete: ");
                    int courseIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    adminActions.deleteCourse(courseIdToDelete);
                    System.out.println("Course deleted successfully.");
                    break;

                case 5:
                    Map<Teacher, List<String>> teacherCourseMap = adminActions.getTeachersWithCourses();
                    for (Map.Entry<Teacher, List<String>> entry : teacherCourseMap.entrySet()) {
                        Teacher teacher = entry.getKey();
                        List<String> courses = entry.getValue();

                        System.out.println("Teacher ID: " + teacher.getTeacherId());
                        System.out.println("Full Name: " + teacher.getFullName());
                        System.out.println("Specialization: " + teacher.getSpecialization());
                        System.out.println("Assigned Courses: " + (courses.isEmpty() ? "None" : String.join(", ", courses)));
                        System.out.println();
                    }
                    break;

                case 6:
                    System.out.print("Enter Teacher ID to update: ");
                    int teacherIdToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Teacher Name: ");
                    String newTeacherName = scanner.nextLine();
                    System.out.print("Enter New Specialization: ");
                    String newSpecialization = scanner.nextLine();
                    adminActions.updateTeacher(new Teacher(teacherIdToUpdate, 0, newTeacherName, newSpecialization));
                    System.out.println("Teacher updated successfully.");
                    break;

                case 7:
                    System.out.print("Enter Teacher ID to delete: ");
                    int teacherIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    adminActions.deleteTeacher(teacherIdToDelete);
                    System.out.println("Teacher deleted successfully.");
                    break;

                case 8:
                    for (var student : adminActions.viewAllStudents()) {
                        System.out.println(student);
                    }
                    break;

                case 9:
                    System.out.print("Enter Student ID to update: ");
                    int studentIdToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Student Name: ");
                    String newStudentName = scanner.nextLine();
                    adminActions.updateStudent(new Student(studentIdToUpdate, 0, newStudentName));
                    System.out.println("Student updated successfully.");
                    break;

                case 10:
                    System.out.print("Enter Student ID to delete: ");
                    int studentIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    adminActions.deleteStudent(studentIdToDelete);
                    System.out.println("Student deleted successfully.");
                    break;

                case 11:  // Add Teacher
                    System.out.print("Enter Teacher Full Name: ");
                    String teacherName = scanner.nextLine();
                    System.out.print("Enter Teacher Specialization: ");
                    String teacherSpecialization = scanner.nextLine();
                    System.out.print("Enter Teacher Username: ");
                    String teacherUsername = scanner.nextLine();
                    System.out.print("Enter Teacher Password: ");
                    String teacherPassword = scanner.nextLine();
                    adminActions.addTeacher(new Teacher(0, 0, teacherName, teacherSpecialization), teacherUsername, teacherPassword);
                    break;

                case 12:  // Add Student
                    System.out.print("Enter Student Full Name: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Enter Student Username: ");
                    String studentUsername = scanner.nextLine();
                    System.out.print("Enter Student Password: ");
                    String studentPassword = scanner.nextLine();
                    adminActions.addStudent(new Student(0, 0, studentName), studentUsername, studentPassword);
                    break;

                case 13:  // Assign Course to Teacher
                    System.out.print("Enter Teacher ID: ");
                    int teacherId = scanner.nextInt();
                    System.out.print("Enter Course ID: ");
                    int courseId = scanner.nextInt();
                    scanner.nextLine();

                    adminActions.assignCourseToTeacher(teacherId, courseId);
                    System.out.println("Course assigned successfully.");
                    break;
                case 14: // Assign Student to Course
                    System.out.print("Enter Student ID: ");
                    int studentId = scanner.nextInt();
                    System.out.print("Enter Course ID: ");
                    int courseIdToAdd = scanner.nextInt();
                    scanner.nextLine();

                    adminActions.assignStudentToCourse(studentId, courseIdToAdd);
                    break;

                case 15:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

//    private static void teacherMenu(Scanner scanner, int teacherId) throws SQLException {
//        while (true) {
//            System.out.println("\n--- Teacher Menu ---");
//            System.out.println("1. View Assigned Courses");
//            System.out.println("2. Logout");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (choice) {
//                case 1:
//                    for (Course course : teacherActions.viewAssignedCourses(teacherId)) {
//                        System.out.println(course);
//                    }
//                    break;
//
//                case 2:
//                    System.out.println("Logging out...");
//                    return;
//
//                default:
//                    System.out.println("Invalid option.");
//            }
//        }
//    }
//private static void teacherMenu(Scanner scanner, int teacherId) throws SQLException {
//    while (true) {
//        System.out.println("\n--- Teacher Menu ---");
//        System.out.println("1. View Assigned Courses");
//        System.out.println("2. Logout");
//
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//
//        switch (choice) {
//            case 1: // View Assigned Courses
//                List<Course> assignedCourses = teacherActions.viewAssignedCourses(teacherId);
//                if (assignedCourses.isEmpty()) {
//                    System.out.println("No courses assigned to you.");
//                } else {
//                    for (Course course : assignedCourses) {
//                        System.out.println("Course ID: " + course.getCourseId());
//                        System.out.println("Course Name: " + course.getCourseName());
//                        System.out.println("Description: " + course.getDescription());
//                        System.out.println("Credits: " + course.getCredits());
//                        System.out.println("---------------------------------");
//                    }
//                }
//                break;
//
//            case 2: // Logout
//                System.out.println("Logging out...");
//                return;
//
//            default:
//                System.out.println("Invalid option.");
//        }
//    }
//}

    private static void teacherMenu(Scanner scanner, int teacherId) throws SQLException {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. View Assigned Courses");
            System.out.println("2. View Students in My Courses");
            System.out.println("3. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // View Assigned Courses
                    List<Course> assignedCourses = teacherActions.viewAssignedCourses(teacherId);
                    if (assignedCourses.isEmpty()) {
                        System.out.println("No courses assigned to you.");
                    } else {
                        for (Course course : assignedCourses) {
                            System.out.println("Course ID: " + course.getCourseId());
                            System.out.println("Course Name: " + course.getCourseName());
                            System.out.println("Description: " + course.getDescription());
                            System.out.println("Credits: " + course.getCredits());
                            System.out.println("---------------------------------");
                        }
                    }
                    break;

                case 2: // View Students in My Courses
                    List<Student> students = teacherActions.viewStudentsForMyCourses(teacherId);
                    if (students.isEmpty()) {
                        System.out.println("No students are enrolled in your courses.");
                    } else {
                        for (Student student : students) {
                            System.out.println("Student ID: " + student.getStudentId());
                            System.out.println("Full Name: " + student.getFullName());
                            System.out.println("---------------------------------");
                        }
                    }
                    break;

                case 3: // Logout
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }


    private static void studentMenu(Scanner scanner, int studentId) throws SQLException {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Enrolled Courses");
            System.out.println("2. View Teachers and Their Courses"); // New Option
            System.out.println("3. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // View Enrolled Courses
                    List<Course> courses = studentActions.viewEnrolledCourses(studentId);
                    if (courses.isEmpty()) {
                        System.out.println("No courses enrolled.");
                    } else {
                        System.out.println("Your Enrolled Courses:");
                        for (Course course : courses) {
                            System.out.println("Course ID: " + course.getCourseId());
                            System.out.println("Course Name: " + course.getCourseName());
                            System.out.println("Description: " + course.getDescription());
                            System.out.println("Credits: " + course.getCredits());
                            System.out.println("---------------------------------");
                        }
                    }
                    break;

                case 2: // View Teachers and Their Courses
                    List<String> teacherCourseList = studentActions.viewTeachersAndCourses();
                    if (teacherCourseList.isEmpty()) {
                        System.out.println("No teachers or courses available.");
                    } else {
                        System.out.println("Teachers and Their Courses:");
                        for (String entry : teacherCourseList) {
                            System.out.println(entry);
                        }
                    }
                    break;

                case 3: // Logout
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }


}
