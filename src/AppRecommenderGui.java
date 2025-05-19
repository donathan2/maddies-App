import javax.swing.*;
import java.awt.*;

public class AppRecommenderGui extends JFrame {

    JPanel inputPanel;
    JLabel inputLabel;
    JTextField inputText;
    JButton reroll;
    JButton surprise;
    String prompt;
    JTextArea output;



    public AppRecommenderGui() {
        setTitle("The Best App Finder in the World");
        setSize(800, 500);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(inputPanel);

        inputLabel = new JLabel("Enter what kind of app you're looking for:");

        reroll = new JButton("More Apps");
        reroll.addActionListener(e -> {
            new Thread(() -> {
                String result = Exporter.chatGPT("The prompt you are about to receive may be an app, or a description of the kind of app " +
                        "that the user is looking for. Respond with 3 apps with a brief but descriptive idea of what they are. If the user seems to respond" +
                        "with something irrelevant, still find 3 most relevant apps. Make sure entries are spaced by 1 line. Do not respond to user. If the user" +
                        "seems to enter the name of an app, don't recommend the same app as that's redundant. This same command may have been called before, so try and select" +
                        "apps that are a little bit less obvious, as then the user won't receive suggestions for the same app." +
                        "The prompt is: " + prompt);

                SwingUtilities.invokeLater(() -> {
                    output.setText(result);
                });
            }).start();
        });

        surprise = new JButton("Surprise me!");
        surprise.addActionListener(e -> {
            new Thread(() -> {
                String result = Exporter.chatGPT("The user has selected the surprise app function. Respond with 3 apps, chosen at" +
                        "complete random (they could be games, social media, cooking, functionality apps, or anything obscure or niche), and provide" +
                        "a brief but descriptive idea of what they are to the user. Keep in mind that this request may be made multiple times" +
                        ", so try and really choose different apps every time that might not have been chosen in previous calls. Make sure entries are spaced by 1 line." +
                        "Also, don't be afraid to choose common apps!");

                SwingUtilities.invokeLater(() -> {
                    output.setText(result);
                });
            }).start();
        });


        inputText = new JTextField(20);
        inputText.addActionListener(e -> {
            prompt = inputText.getText();
            new Thread(() -> {
                String result = Exporter.chatGPT("The prompt you are about to receive may be an app, or a description of the kind of app " +
                        "that the user is looking for. Respond with 3 apps with a brief but descriptive idea of what they are. If the user seems to respond" +
                        "with something irrelevant, still find 3 most relevant apps. Make sure entries are spaced by 1 line. Do not respond to user. If the user" +
                        "seems to enter the name of an app, don't recommend the same app as that's redundant." +
                        "The prompt is: " + prompt);

                // Now safely update the UI on the Event Dispatch Thread
                SwingUtilities.invokeLater(() -> {
                    output.setText(result);
                    System.out.println(output.getText());
                });
            }).start();
        });


        inputPanel.add(inputLabel);
        inputPanel.add(inputText);
        inputPanel.add(reroll);
        inputPanel.add(surprise);

        output = new JTextArea();
        output.setPreferredSize(new Dimension(100, 20));
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        this.add(output);



        setVisible(true);




    }
}
