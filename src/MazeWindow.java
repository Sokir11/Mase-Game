import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MazeWindow extends JFrame {
    private MazePanel mazePanel;
    private JButton checkSolutionButton;
    private Timer animationTimer;
    private List<Point> solutionPath;
    private int currentStep = 0;

    public MazeWindow(MazeModel mazeModel, RenderConfig config, List<Point> solutionPath) {
        this.solutionPath = solutionPath;

        setTitle("Resolved Maze View");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // יצירת פאנל הציור
        mazePanel = new MazePanel(mazeModel, config, solutionPath);

        // עטיפת הפאנל ב-JScrollPane כדי לאפשר גלילה נוחה למבוכים בגודל 20x20 לתא
        JScrollPane scrollPane = new JScrollPane(mazePanel);
        add(scrollPane, BorderLayout.CENTER);

        // פאנל תחתון לכפתור הבדיקה
        JPanel bottomPanel = new JPanel();
        checkSolutionButton = new JButton("Check Solution");
        bottomPanel.add(checkSolutionButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // הגדרת טיימר לאנימציה
        int delay = config.getAnimationDelay();
        if (delay <= 0) delay = 50;

        animationTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep <= solutionPath.size()) {
                    mazePanel.setCurrentStepIndex(currentStep);
                    currentStep++;
                } else {
                    animationTimer.stop();
                }
            }
        });

        // פעולת לחיצה על Check Solution
        checkSolutionButton.addActionListener(e -> {
            if (solutionPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No solution path found for this maze!", "Result", JOptionPane.WARNING_MESSAGE);
                return;
            }
            checkSolutionButton.setEnabled(false);
            currentStep = 0;
            animationTimer.start();
        });
    }
}