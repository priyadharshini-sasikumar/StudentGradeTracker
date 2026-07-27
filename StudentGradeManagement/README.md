# Student Grade Management System 🎓

A complete, production-grade desktop application built in Java using Object-Oriented Programming (OOP) principles and a clean Model-View-Controller (MVC) architecture. Features a modern, highly polished Java Swing GUI, statistical dashboard, real-time input validation, custom-painted grade distribution charts, and official student report card generation with automatic file persistence.

---

## 🌟 Key Features

### 🔐 1. Secure Authentication
- Split-hero styled login interface with default administrator credentials.
- **Username:** `Priyadharshini`
- **Password:** `admin123`
- Includes input validation, error alerts, and password visibility toggle.

### 📊 2. Dynamic Dashboard
- **Summary Metrics Cards:** Real-time display of **Total Students**, **Class Average**, **Highest Score**, and **Pass Rate**.
- **Grade Distribution Chart:** Custom painted interactive bar chart (`A+`, `A`, `B`, `C`, `D`, `F`).
- **Top Performers Leaderboard:** Displays top scoring students ordered by average percentage.

### 🎓 3. Student Management (CRUD)
- Complete student management: **Add Student**, **Update Student**, **Delete Student** (with confirmation modal), **Search Student** (by ID or Name), and **Clear Form**.
- **Data Fields:** Student ID, Name, Department, Year, Section, and Marks for 5 subjects (*Mathematics*, *Science/Physics*, *English*, *History*, *Computer Science*).
- **Auto Calculations:** Total Marks, Average Percentage, Letter Grade (`A+` to `F`), GPA (4.0 scale), and Pass/Fail status.
- **Input Validation:** Enforces non-empty fields and strictly validates marks between `0` and `100`.

### 📈 4. Reports & Analytics Module
- Spotlight cards for **Highest Scoring Student**, **Lowest Scoring Student**, and **Pass/Fail Summary**.
- Department-wise aggregated summary breakdown table.
- **Official Student Report Card Generator:** Interactive modal dialog rendering a printable/exportable report card for any selected student.

### 💾 5. File Persistence & Sample Data
- Automatic file serialization (`data/students.dat`). Records persist across application launches.
- Seeds 10 realistic pre-populated student records if data file is absent.

### ⌨️ 6. Keyboard Shortcuts
- `Ctrl + D`: Navigate to Dashboard
- `Ctrl + M`: Navigate to Student Management
- `Ctrl + R`: Navigate to Reports
- `Ctrl + Q`: Logout

---

## 📂 Project Architecture & Package Structure

```
StudentGradeManagement/
├── src/
│   └── com/
│       └── grade/
│           ├── main/
│           │   └── Main.java                    # Application entry point & Look & Feel
│           ├── model/
│           │   └── Student.java                 # Encapsulated Student model class
│           ├── service/
│           │   ├── StudentService.java          # In-memory logic, CRUD, filtering & analytics
│           │   └── AuthService.java             # Admin login authentication
│           ├── util/
│           │   ├── GradeCalculator.java         # Math helper for total, avg, grade, GPA
│           │   ├── FileUtil.java                # Object serialization & sample data loader
│           │   └── UIUtils.java                 # Design system (colors, custom components)
│           └── ui/
│               ├── LoginFrame.java              # Login screen
│               ├── MainFrame.java               # Main window with sidebar navigation
│               ├── DashboardPanel.java          # Analytics dashboard & custom chart
│               ├── StudentManagementPanel.java  # Student CRUD & JTable
│               ├── ReportsPanel.java            # Class reports & department breakdown
│               └── ReportCardDialog.java        # Modal official student report card
├── bin/                                         # Compiled class files
├── data/
│   └── students.dat                             # Serialized binary data storage
├── StudentGradeManagement.jar                   # Runnable JAR executable
├── run.bat                                      # Windows double-click launcher
├── run.sh                                       # Linux/macOS launcher script
└── README.md                                    # Comprehensive project documentation
```

---

## 🚀 How to Run the Application

### Option 1: Direct Executable (Recommended)
Double-click `run.bat` (on Windows) or run `./run.sh` (on Linux/macOS).

### Option 2: Using Runnable JAR
Execute the following command in terminal:
```bash
java -jar StudentGradeManagement.jar
```

### Option 3: Compile & Run from Source Code
```bash
# 1. Compile source files
javac -d bin -sourcepath src src/com/grade/main/Main.java

# 2. Run Main class
java -cp bin com.grade.main.Main
```

---

## 🛠️ Design System & Color Palette
- **Sidebar Background:** `#1E1E2D` (Dark Navy)
- **Canvas Background:** `#F4F6F9` (Soft Gray)
- **Primary Accent:** `#3699FF` (Modern Blue)
- **Success Accent:** `#1BC5BD` (Emerald Green)
- **Warning Accent:** `#FFA800` (Amber)
- **Danger Accent:** `#F64E60` (Vibrant Red)
