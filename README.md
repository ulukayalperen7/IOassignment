# Java Course Management System
This Java program provides functionality for managing course details, student scores, and grading. It allows users to input course details, student scores, and generates student grades based on predefined grading criteria.

## Features
- Course Details Management: Read course details from a file including category, quantity, and weight of each category.
- Student Scores Input: Read student names and their corresponding scores from a file.
- Grading: Calculate grades for each student based on their scores and course details.
- Error Logging: Log errors encountered during grading process to an error log file.

```java
public class IOtesting {
    // Class definition and methods...
    public static void main(String[] args) {
        // Main method...
    }
}
```
### Usage

#### Running the Program
To run the program, use the following command:

```bash
java IOtesting <baseFilename>
```
or

```java 
java IOtesting <courseDetailsFilename> <studentScoresFilename> <studentGradesFilename> <errorLogFilename>
```
Replace **baseFilename** with the desired base filename, or provide filenames for course details, student scores, student grades, and error log.

### Input File Formats
- Course Details File: Each line should contain category, quantity, and weight separated by spaces.
Example:

```bash
Category1 3 30
Category2 2 20
Category3 1 50
```
- Student Scores File: Each line should contain student name and score separated by a space.
Example:
```bash
John 85
Alice 75
Bob 90
```

#### Output Files

- **Student Grades File:** Contains student names along with their calculated grades, grade letters, GPA points, and pass status.
- **Error Log File:** Logs any errors encountered during the grading process.
#### Contributing

Contributions are welcome! If you have any suggestions, bug reports, or feature requests, please open an issue on GitHub.
#### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.