# Teacher-Feedback-Form-in-Java
Teacher Feedback Form in Java using AWT

## ⚙️ Setting Up the MySQL JDBC Driver in VS Code

To compile and run this project, you must add the MySQL Connector/J `.jar` file to your Java project's classpath.

### Method 1: Using the Java Projects Explorer (Recommended)

1.  In the VS Code sidebar, open the **JAVA PROJECTS** view.
2.  Expand your project.
3.  Find the **Referenced Libraries** section.
4.  Hover your mouse over it and click the **`+`** (plus) icon.
5.  Browse to your downloaded `mysql-connector-j-x.x.x.jar` file and select it.

This method updates the project's classpath automatically.

### Method 2: Using the Command Palette

1.  Inside your VS Code window, open the Command Palette using the shortcut:
    * **Windows/Linux:** `Ctrl+Shift+P`
    * **Mac:** `Cmd+Shift+P`
2.  A search bar will appear at the top of your VS Code window.
3.  Start typing **`Java: Configure Classpath`** and select it from the list.
4.  This will open the classpath configuration UI (or the `.vscode/settings.json` file), allowing you to add the `mysql-connector-j-x.x.x.jar` file to your referenced libraries.

---

### ⚠️ Troubleshooting: "Java: is not recognized" Error

If you try to type `Java: Configure Classpath` directly into your computer's terminal (like PowerShell, CMD, or Bash), you will get an error similar to this:

