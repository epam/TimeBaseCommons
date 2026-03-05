package deltix.util.swing.dialog;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import deltix.util.swing.GBC;

import javax.swing.*;
import java.awt.*;

/**
 * User: TurskiyS
 * Date: 10/21/13
 */
public class AuthorizationDialog extends AbstractDialog {

    private String         user;
    private String         password;

    private JTextField     userField;
    private JPasswordField passField;

    private String         msg;

    public AuthorizationDialog(final Window owner,
                               String name,
                               String title,
                               String msg,
                               String actionButtonTitle) {
        super(owner, name, title, actionButtonTitle);
        this.msg = msg;

        initializeGUI();
    }

    public Component createUI(){
        JPanel form = new JPanel(new GridBagLayout());

        int yC = 0;

        GBC gc= new GBC().setWeight(0,0).setInsets(5,5,15,5);

        userField = new JTextField();
        passField = new JPasswordField();

        String labelText = "<html><div width=\"200px\">" +
                "\t"+msg +
                "</div><html>";

        form.add(new JLabel(labelText), gc.setPosition(0,yC++).setSpan(2, 1));

        form.add(new JLabel("User:"), gc.setPosition(0, yC).
                setAnchor(GBC.EAST).
                setSpan(1, 1).
                setInsets(5, 5, 0, 5));

        form.add(userField, gc.setPosition(1,yC++).
                                setWeight(1,0).
                                setFill(GBC.HORIZONTAL)
                                );

        form.add(new JLabel("Password:"), gc.setPosition(0,yC).
                                            setWeight(0, 0).
                                            setFill(GBC.NONE));

        form.add(passField, gc.setPosition(1,yC).
                                setWeight(1, 0).
                                setFill(GBC.HORIZONTAL));

        return form;
    }

    @Override
    protected void initializeGUI() {
        super.initializeGUI();
        setResizable(false);
        setPreferredSize(new Dimension(300,
                150));
    }

    @Override
    public boolean process() {
        user = userField.getText();
        password = new String(passField.getPassword());
        return true;
    }

    public JPanel createButtonPanel(){
        final ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addButton(mainAction);
        builder.addButton(cancelAction);

        return  builder.getPanel();
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }
}

