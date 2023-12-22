
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MergeFinal {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/university_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String username = null;  // Declare the username variable outside the block

        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("\tWELCOME TO HARAMAYA UNIVERSITY ACADEMIC MANAGEMENT SYSTEM!");
            System.out.println("----------------------------------------------------------------");
            System.out.println("Are you a student or a teacher?");
            System.out.print("==>>");
            String userType;
            boolean validUserType = false;
            do {
                userType = scanner.nextLine();

                if ("student".equalsIgnoreCase(userType) || "teacher".equalsIgnoreCase(userType)) {
                    validUserType = true;
                } else {
                    System.out.println("Invalid user type. Please Re-enter 'student' or 'teacher'.");
                }
            } while (!validUserType);

            if ("student".equalsIgnoreCase(userType)) {
                // Student Functionality
                boolean exit = false;

                while (!exit) {
                    System.out.println("1. To Register\n2. To See Schedule\n3. To See Your Grade\n4. To See Materials\n5. Exit\nEnter your choice:");

                    int studentChoice = 0;
                    boolean validChoice = false;

                    do {
                        if (scanner.hasNextInt()) {
                            studentChoice = scanner.nextInt();

                            if (studentChoice >= 1 && studentChoice <= 5) {
                                validChoice = true;
                            } else {
                                System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number between 1 and 5.");
                            scanner.next(); // Clear the invalid input from the scanner
                        }
                    } while (!validChoice);

                    switch (studentChoice) {
                        case 1:
                            // Register Student
                            System.out.println("\tSTUDENTS REGISTRATION IS ONGOING...\n***************************************** \nEnter your username:");
                            username = scanner.next();
                            System.out.println("Enter your password:");
                            String password = scanner.next();
                            registerStudent(connection, username, password);
                            System.out.println("\tYou have been registered successfully!");
                            break;

                        case 2:
                            // See Schedule for Student
                            boolean scheduleExit = false;

                            while (!scheduleExit) {
                                System.out.println("1. Class Schedule\n2. Assignment Schedule\n3. Exam Schedule\n4. Back\nEnter your choice:");

                                int scheduleType = 0;
                                boolean validScheduleChoice = false;

                                do {
                                    if (scanner.hasNextInt()) {
                                        scheduleType = scanner.nextInt();

                                        if (scheduleType >= 1 && scheduleType <= 4) {
                                            validScheduleChoice = true;
                                        } else {
                                            System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                                        }
                                    } else {
                                        System.out.println("Invalid input. Please enter a number between 1 and 4.");
                                        scanner.next(); // Clear the invalid input from the scanner
                                    }
                                } while (!validScheduleChoice);

                                switch (scheduleType) {
                                    case 1:
                                        // Display Class Schedule
                                        viewClassScheduleForStudent(connection);
                                        break;

                                    case 2:
                                        // Display Assignment Schedule (implement logic)
                                        viewAssignmentScheduleForStudent(connection);
                                        System.out.println("Assignment Schedule functionality is not implemented yet.");
                                        break;

                                    case 3:
                                        // Display Exam Schedule (implement logic)
                                        viewExamScheduleForStudent(connection);

                                        break;

                                    case 4:
                                        // Go back
                                        scheduleExit = true;
                                        break;

                                    default:
                                        System.out.println("Invalid choice");
                                }
                            }
                            break;

                        case 3:
                            // See Grade
                            System.out.println("Enter your username:");
                            username = scanner.next();
                            viewGradesForStudent(connection, username);
                            break;

                        case 4:
                            // See Material
                            viewMaterials(connection);
                            break;

                        case 5:
                            // Exit the student functionality
                            exit = true;
                            break;

                        default:
                            System.out.println("Invalid choice");
                    }
                }
            }else if ("teacher".equalsIgnoreCase(userType)) {
                boolean TeacherFunctionality = true;

                while (TeacherFunctionality) {
                    System.out.println("1. Upload Schedule\n2. Upload Grade\n3. Upload Material\nEnter your choice:");
                    int teacherChoice = scanner.nextInt();

                    switch (teacherChoice) {
                        case 1:
                            // Upload Schedule
                            boolean continueScheduleFunctionality = true;

                            while (continueScheduleFunctionality) {
                                System.out.println("  1. Upload Class Schedule\n  2. Upload Assignment Schedule\n  3. Upload Exam Schedule\nEnter your choice:");
                                int scheduleChoice = scanner.nextInt();

                                switch (scheduleChoice) {
                                    case 1:
                                        // Upload Class Schedule
                                        uploadClassSchedule(connection);
                                        continueScheduleFunctionality = false;
                                        break;

                                    case 2:
                                        // Upload Assignment Schedule
                                        uploadAssignmentSchedule(connection);
                                        continueScheduleFunctionality = false;
                                        break;

                                    case 3:
                                        // Upload Exam Schedule
                                        uploadExamSchedule(connection);
                                        continueScheduleFunctionality = false;
                                        break;

                                    default:
                                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                                }
                            }
                            break;

                        case 2:
                            // Upload Grade
                            uploadGrade(connection);
                            break;

                        case 3:
                            uploadMaterial(connection);
                            break;

                        default:
                            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }

                    // Ask the user if they want to continue the teacher functionality
                    System.out.println("Do you want to continue the teacher functionality? (y/n)");
                    String continueChoice = scanner.nextLine();

                    if (!continueChoice.equalsIgnoreCase("y")) {
                        TeacherFunctionality = false;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error");
        } finally {
            // Close the scanner in the finally block to ensure it's always closed
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    // Method to upload exam schedule
    private static void uploadExamSchedule(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Get input from the user
            System.out.println("Enter subject:");
            String subject = scanner.nextLine();

            System.out.println("Enter exam name:");
            String examName = scanner.nextLine();

            System.out.println("Enter exam date (yyyy-MM-dd):");
            String examDateStr = scanner.nextLine();
            LocalDate examDate = LocalDate.parse(examDateStr);

            System.out.println("Enter exam time:");
            String examTime = scanner.nextLine();

            // Prepare and execute SQL statement to insert data into exam_schedule table
            String insertQuery = "INSERT INTO exam_schedule (subject, exam_name, exam_date, exam_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, subject);
                preparedStatement.setString(2, examName);
                preparedStatement.setDate(3, Date.valueOf(examDate));
                preparedStatement.setString(4, examTime);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Exam schedule uploaded successfully.");
                } else {
                    System.out.println("Failed to upload exam schedule.");
                }
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
            System.out.println("Error uploading exam schedule.");
        } finally {
            scanner.close();
        }
    }


    // ...// Method to view exam schedule for student
    private static void viewExamScheduleForStudent(Connection connection) {
        try {
//        // Perform a SELECT query to retrieve exam schedule from the database
            String query = "SELECT * FROM exam_schedule";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

//            // Display the exam schedule in a table form
                System.out.printf("%-15s %-15s %-15s %-15s%n", "Subject", "Exam Name", "Exam Date", "Exam Time");     System.out.println("-----------------------------------------------------------");

                while (resultSet.next()) {
                    String subject = resultSet.getString("subject");
                    String examName = resultSet.getString("exam_name");
                    String examDate = resultSet.getString("exam_date");
                    String examTime = resultSet.getString("exam_time");

                    System.out.printf("%-15s %-15s %-15s %-15s%n", subject, examName, examDate, examTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching exam schedule");
        }
    }


    // Common methods for both student and teacher
    // ... (remaining methods remain unchanged)



    // Common methods for both student and teacher
    private static void registerStudent(Connection connection, String username, String password) throws SQLException {
        String insertUserQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            System.out.println("Student registration successful!");
        }
    }

    private static void viewClassScheduleForStudent(Connection connection) throws SQLException {
        // Implement logic to retrieve and display class schedule for students from the database
        // Use PreparedStatement to execute queries
        String selectClassScheduleQuery = "SELECT subject, days, class_time FROM class_schedule";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectClassScheduleQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Class Schedule:");
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No class schedule found.");
            } else {
                System.out.printf("%-15s %-15s %-15s%n", "Subject", "Days", "Class Time");
                while (resultSet.next()) {
                    String subject = resultSet.getString("subject");
                    String classDate = resultSet.getString("days");
                    String classTime = resultSet.getString("class_time");
                    System.out.printf("%-15s %-15s %-15s%n", subject, classDate, classTime);
                }
            }
        }
    }


    private static void viewGradesForStudent(Connection connection, String studentUsername) {
        try {
            // Check if the student username exists in the users table
            if (isStudentExists(connection, studentUsername)) {
                // Display grades for the specified student
                System.out.println("Grades for Student " + studentUsername + ":");

                // List of subjects
                List<String> subjects = List.of("data_structure", "oop_java", "statistics", "coa", "networking", "operating_system");

                // Display header
                System.out.printf("%-20s %-10s %-10s%n", "Subject", "Mark", "Grade");
                System.out.println("--------------------------------------------");


                for (String subject : subjects) {
                    // Display grades for each subject
                    displayGrades(connection, studentUsername, subject);
                }
            } else {
                System.out.println("Student with the username " + studentUsername + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving grades from the database");
        }
    }

    private static void displayGrades(Connection connection, String studentUsername, String subject) throws SQLException {
        // Query to get grades for the specified student and subject
        String selectGradesQuery = "SELECT mark, grade FROM " + subject + " WHERE student_username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectGradesQuery)) {
            preparedStatement.setString(1, studentUsername);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    double mark = resultSet.getDouble("mark");
                    String grade = resultSet.getString("grade");
                    System.out.printf("%-20s %-10s %-10s%n", subject, mark, grade);
                }
            }
        }
    }
    // Method to view assignment schedule for student
    private static void viewAssignmentScheduleForStudent(Connection connection) {
        try {
            // Perform a SELECT query to retrieve assignment schedule from the database
            String query = "SELECT * FROM Assignment_Schedule";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Display the assignment schedule in a table form
                System.out.printf("%-15s %-15s %-15s%n", "Subject", "Start Date", "Deadline");
                System.out.println("--------------------------------------------------");

                while (resultSet.next()) {
                    String subject = resultSet.getString("subject");
                    String startDate = resultSet.getString("start_date");
                    String deadline = resultSet.getString("deadline");

                    System.out.printf("%-15s %-15s %-15s%n", subject, startDate, deadline);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching assignment schedule");
        }
    }


    private static void viewMaterials(Connection connection) {
        // Implement logic to retrieve and display materials from the database
        String selectMaterialsQuery = "SELECT subject, material_name FROM materials";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectMaterialsQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Materials:");
            System.out.printf("%-15s  %-15s%n", "Material Name             ", "Subject");
            System.out.println("------------------------------------------------------");

            while (resultSet.next()) {
                String materialName = resultSet.getString("material_name");
                String subject = resultSet.getString("subject");

                System.out.printf("%-15s  %-15s%n", materialName, subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving materials from the database");
        }
    }


    private static void uploadClassSchedule(Connection connection) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the subject:");
            String subject = scanner.nextLine();

            System.out.println("Enter the class days :");
            String classDate = scanner.next();

            System.out.println("Enter the class time (HH:mm:ss):");
            String classTime = scanner.next();


// Store class schedule in the database
            String insertClassScheduleQuery = "INSERT INTO class_schedule (subject, days, class_time, user_id) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertClassScheduleQuery)) {
                preparedStatement.setString(1, subject);
                preparedStatement.setString(2, classDate);
                preparedStatement.setString(3, classTime);
                // Assuming you have a user_id for the teacher, replace 1 with the actual user_id
                preparedStatement.setInt(4, 1);
                preparedStatement.executeUpdate();
                System.out.println("Class schedule uploaded successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error uploading class schedule to the database");
            }
        }
    }


    private static void uploadGrade(Connection connection) {
        try (Scanner scanner = new Scanner(System.in)) {
            String subject;
            String studentUsername;

            // Continue asking for input until "exit" is entered
            while (true) {
                System.out.println("Enter the subject (type 'exit' to finish):");
                subject = scanner.next();

                if ("exit".equalsIgnoreCase(subject)) {
                    break; // Exit the loop if "exit" is entered
                }

                System.out.println("Enter the student username:");
                studentUsername = scanner.next();

                try {
                    if (isStudentExists(connection, studentUsername)) {
                        // Store grade in the database
                        System.out.println("Enter the grade:");
                        double mark = scanner.nextDouble();

                        // Move the consumption of the newline character here
                        scanner.nextLine();

                        uploadGradeToDatabase(connection, subject, studentUsername, mark);
                        // Print the message only once after successful grade upload
                        // System.out.println("Grade uploaded successfully!");
                    } else {
                        System.out.println("Student with the username " + studentUsername + " not found.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error checking if student exists");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid value.");
                    // Move the consumption of the newline character here
                    scanner.nextLine();
                }
            }
        }
    }




    private static boolean isStudentExists(Connection connection, String username) throws SQLException {
        String checkUserQuery = "SELECT COUNT(*) AS count FROM users WHERE username = ? AND role = 'student'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkUserQuery)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt("count");
                return count > 0;
            }
        }
    }
    // Method to upload assignment schedule
    private static void uploadAssignmentSchedule(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Get input from the user
            System.out.println("Enter subject:");
            String subject = scanner.nextLine();

            System.out.println("Enter start date (yyyy-MM-dd):");
            String startDateStr = scanner.nextLine();
            LocalDate startDate = LocalDate.parse(startDateStr);


            System.out.println("Enter deadline (yyyy-MM-dd):");
            String deadlineStr = scanner.nextLine();
            LocalDate deadline = LocalDate.parse(deadlineStr);

            // Prepare and execute SQL statement to insert data into Assignment_Schedule table
            String insertQuery = "INSERT INTO Assignment_Schedule (subject, start_date, deadline) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, subject);
                preparedStatement.setDate(2, Date.valueOf(startDate));
                preparedStatement.setDate(3, Date.valueOf(deadline));

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Assignment schedule uploaded successfully.");
                } else {
                    System.out.println("Failed to upload assignment schedule.");
                }
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
            System.out.println("Error uploading assignment schedule.");
        } finally {
            scanner.close();
        }
    }



    // Inside the uploadGradeToDatabase method
    // Inside the uploadGradeToDatabase method
    private static void uploadGradeToDatabase(Connection connection, String subject, String studentUsername, double mark) {
        try {
            // Check if the grade already exists for the same username and subject
            if (isGradeExists(connection, subject, studentUsername)) {
                // Update the existing grade
                updateGrade(connection, subject, studentUsername, mark);
                System.out.println("Grade updated successfully!");
            } else {
                // Calculate grade based on the provided criteria
                String grade = calculateGrade(mark);

                // Store grade in the respective subject table
                String insertGradeQuery = "INSERT INTO " + subject + " (subject_name, student_username, mark, grade) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertGradeQuery)) {
                    preparedStatement.setString(1, subject);
                    preparedStatement.setString(2, studentUsername);
                    preparedStatement.setDouble(3, mark);
                    preparedStatement.setString(4, grade);
                    preparedStatement.executeUpdate();
                    System.out.println("Grade uploaded successfully!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error checking/updating grade in the database");
        }
    }
    private static void uploadMaterial(Connection connection) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the subject:");
            String subject = scanner.nextLine();

            System.out.println("Enter the material name:");
            String materialName = scanner.nextLine();

            // Store material in the database
            String insertMaterialQuery = "INSERT INTO materials (subject, material_name) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertMaterialQuery)) {
                preparedStatement.setString(1, subject);
                preparedStatement.setString(2, materialName);
                preparedStatement.executeUpdate();
                System.out.println("Material uploaded successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error uploading material to the database");
            }
        }
    }


    private static boolean isGradeExists(Connection connection, String subject, String studentUsername) {
        String checkGradeQuery = "SELECT COUNT(*) AS count FROM " + subject + " WHERE student_username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkGradeQuery)) {
            preparedStatement.setString(1, studentUsername);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking if grade exists");
            return false;  // Return false in case of an error
        }
    }



    private static void updateGrade(Connection connection, String subject, String studentUsername, double mark) {
        // Calculate grade based on the provided criteria
        String grade = calculateGrade(mark);

        // Update the grade in the respective subject table
        String updateGradeQuery = "UPDATE " + subject + " SET mark = ?, grades = ? WHERE student_username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateGradeQuery)) {
            preparedStatement.setDouble(1, mark);
            preparedStatement.setString(2, grade);
            preparedStatement.setString(3, studentUsername);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating grade in the database");
        }
    }

    private static String calculateGrade(double mark) {
        if (mark >= 90) {
            return "A+";
        } else if (mark >= 85) {
            return "A";
        } else if (mark >= 80) {
            return "A-";
        } else if (mark >= 75) {
            return "B+";
        } else if (mark >= 70) {
            return "B";
        } else if (mark >= 65) {
            return "C+";
        } else if (mark >= 60) {
            return "C";
        } else if (mark >= 55) {
            return "C-";
        } else if (mark >= 50) {
            return "D";
        } else {
            return "F";
        }
    }
}