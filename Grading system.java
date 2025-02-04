import javax.swing.*; 
import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;

class Subject {
    String name;
    String grade;
    int credits;

    public Subject(String name, String grade, int credits) {
        this.name = name;
        this.grade = grade;
        this.credits = credits;
    }

    @Override
    public String toString() {
        return name + " (" + credits + " credits, " + grade + ")";
    }
}

class SubjectManager {
    private List<Subject> subjects;

    public SubjectManager() {
        this.subjects = new ArrayList<>();
    }

    public void addSubject(String name, String grade, int credits) {
        Subject subject = new Subject(name, grade, credits);
        subjects.add(subject);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}

class CGPACalculator {
    public double calculateCGPA(List<Subject> subjects) {
        if (subjects.isEmpty()) {
            throw new RuntimeException("Add at least one subject.");
        }

        Map<String, Integer> gradePoints = new HashMap<>();
        gradePoints.put("O", 10);
        gradePoints.put("A+", 9);
        gradePoints.put("A", 8);
        gradePoints.put("B+", 7);
        gradePoints.put("B", 6);
        gradePoints.put("C+", 5);
        gradePoints.put("C", 4);
        gradePoints.put("D+", 3);
        gradePoints.put("D", 2);
        gradePoints.put("F", 0);

        int totalCredits = subjects.stream().mapToInt(subject -> subject.credits).sum();
        int totalWeightedGradePoints = subjects.stream()
                .mapToInt(subject -> gradePoints.get(subject.grade) * subject.credits)
                .sum();

        return (double) totalWeightedGradePoints / totalCredits;
    }
}

public class CGPACalculatorGUI {
    private SubjectManager subjectManager;
    private CGPACalculator cgpaCalculator;

    private JFrame frame;
    private JTextField subjectNameField;
    private JComboBox<String> gradeComboBox;
    private JTextField creditsField;
    private JTextArea subjectsTextArea;
    private JLabel resultLabel;

    public CGPACalculatorGUI() {
        this.subjectManager = new SubjectManager();
        this.cgpaCalculator = new CGPACalculator();
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("CGPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 2));

        JLabel subjectNameLabel = new JLabel("Enter Subject:");
        subjectNameField = new JTextField();
        JLabel gradeLabel = new JLabel("Select Grade:");
        String[] gradeOptions = {"O", "A+", "A", "B+", "B", "C+", "C", "D+", "D", "F"};
        gradeComboBox = new JComboBox<>(gradeOptions);
        JLabel creditsLabel = new JLabel("Enter Credits:");
        creditsField = new JTextField();
        JButton addSubjectButton = new JButton("Add Subject");
        JButton calculateCGPAButton = new JButton("Calculate CGPA");
        subjectsTextArea = new JTextArea();
        resultLabel = new JLabel();

        addSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = subjectNameField.getText();
                    String grade = (String) gradeComboBox.getSelectedItem();
                    int credits = Integer.parseInt(creditsField.getText());

                    subjectManager.addSubject(name, grade, credits);
                    updateSubjectsTextArea();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid credits value. Please enter a valid integer.");
                }
            }
        });

        calculateCGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double cgpa = cgpaCalculator.calculateCGPA(subjectManager.getSubjects());
                    resultLabel.setText("CGPA: " + String.format("%.2f", cgpa));
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        frame.add(subjectNameLabel);
        frame.add(subjectNameField);
        frame.add(gradeLabel);
        frame.add(gradeComboBox);
        frame.add(creditsLabel);
        frame.add(creditsField);
        frame.add(addSubjectButton);
        frame.add(calculateCGPAButton);
        frame.add(new JScrollPane(subjectsTextArea));
        frame.add(resultLabel);

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private void updateSubjectsTextArea() {
        subjectsTextArea.setText("");
        for (Subject subject : subjectManager.getSubjects()) {
            subjectsTextArea.append(subject.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CGPACalculatorGUI();
            }
        });
    }
}
