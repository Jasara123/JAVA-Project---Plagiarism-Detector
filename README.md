# Plagiarism Detector

A Java Swing desktop application that compares two text files and reports how much they overlap. Users register and log in, upload two files, and get a plagiarism percentage that is saved to a MySQL database and shown in a history table on their home screen.

Built for **CSE-309 – Advanced Java Programming**.

## Features

- User registration and login (backed by MySQL)
- Upload two text files for comparison
- Plagiarism detection using the **Longest Common Subsequence (LCS)** algorithm on word tokens
- Percentage result displayed in a results window
- Per-user history of compared files and scores

## How It Works

1. Each uploaded file is read, lowercased, and split into words (`split("\\W+")`).
2. `PlagiarismDetector.lcs()` builds a dynamic-programming table over the two word arrays and backtracks to recover the longest common sequence of words.
3. `ResultPanel` converts that into a percentage:

   ```
   percentage = (length of LCS string / (max word count of the two files * 5)) * 100
   ```

   The `* 5` factor approximates the average word length plus a space, so the LCS string length (in characters) can be compared against the longer document's word count. The value is capped at 100%.
4. The percentage is stored in the `files` table and shown in the user's history.

## Project Structure

```
.
├── Main.java                         # Entry point
├── mysql-connector-j-26_10_0.jar     # MySQL JDBC driver
├── MainCode/
│   ├── User.java                     # Auth logic + DB connection constants
│   ├── SuperUser.java                # Placeholder subclass of User (empty)
│   └── PlagiarismDetector.java       # LCS algorithm
└── WindowsCode/
    ├── Index.java                    # Landing window: Log In / Inscription / Exit
    ├── Home.java                     # Main window: history table, compare, logout
    ├── ControlPanel.java             # File upload window
    └── ResultPanel.java              # Result window
```

## Requirements

- JDK 11 or newer (the code uses `Files.readString`)
- MySQL Server running locally on port `3306`
- MySQL Connector/J (included: `mysql-connector-j-26_10_0.jar`)

## Database Setup

The app connects to `jdbc:mysql://127.0.0.1:3306/Plagiarism_detection` as `root` with an empty password (see `MainCode/User.java`). Create the database and tables first. The schema below is inferred from the queries in the code:

```sql
CREATE DATABASE Plagiarism_detection;
USE Plagiarism_detection;

CREATE TABLE profils (
    username      VARCHAR(100) PRIMARY KEY,
    user_password VARCHAR(255) NOT NULL
);

CREATE TABLE files (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    username         VARCHAR(100) NOT NULL,
    file_name1       VARCHAR(255),
    file_name2       VARCHAR(255),
    percentage_plagia DOUBLE DEFAULT 0
);
```

If your MySQL credentials differ, edit `DB_URL`, `DB_USER`, and `DB_PASSWORD` in `MainCode/User.java`.

## Configuration: Upload Folder

Uploaded files are copied to a hardcoded folder in `WindowsCode/ControlPanel.java`:

```java
new File("/Users/andrrianarivonya.andryjasara/Documents/.../Project/Output/" + fichierSource.getName());
```

**Change this to a folder that exists on your machine** (for example `./Output/`) before running, otherwise uploads will fail.

## Build and Run

From the project root:

**macOS / Linux**

```bash
mkdir -p out Output
javac -cp mysql-connector-j-26_10_0.jar -d out MainCode/*.java WindowsCode/*.java Main.java
java -cp out:mysql-connector-j-26_10_0.jar Main
```

**Windows (PowerShell / cmd)**

```bat
mkdir out Output
javac -cp mysql-connector-j-26_10_0.jar -d out MainCode\*.java WindowsCode\*.java Main.java
java -cp "out;mysql-connector-j-26_10_0.jar" Main
```

## Usage

1. Launch the app and click **Inscription** to create an account.
2. Click **Log In** with your credentials.
3. On the Home screen, click **Compare New Files**.
4. Click **Upload File** and choose the first file, then the second file when the dialog reappears.
5. Click **Result** to see the plagiarism percentage.
6. Use **Back** to return to the Home screen or **Exit** to close the app.

## Known Limitations

- **Passwords are stored in plain text.** Hash them (e.g. BCrypt) before any real use.
- **Database credentials are hardcoded**, and the default uses `root` with no password.
- **The history table on Home is not initialized**: `fileTable` in `Home.java` is never assigned a `JTable`/`DefaultTableModel`, so the table will not display and `refreshTable()` will throw a `NullPointerException`.
- **Results are keyed by username only.** `ResultPanel` runs `UPDATE files ... WHERE username = ?`, which updates *every* row for that user rather than just the latest comparison. Using the row `id` would fix this.
- Uploading a second pair of files inserts a new row, but cancelling the file dialog for file 2 can leave a row with a missing `file_name2`.
- Only plain-text files are supported; the comparison is word-based and case-insensitive.
- `SuperUser` is currently an empty class.

## Possible Improvements

- Hash passwords and move DB config to a properties file or environment variables
- Fix the history table and per-row result updates
- Replace absolute layouts (`setLayout(null)`) with layout managers
- Support PDF/DOCX input
- Add admin features via `SuperUser`

## License

Add your preferred license here.
