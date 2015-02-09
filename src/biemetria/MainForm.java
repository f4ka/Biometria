
package biemetria;

import Components.Button;
import java.io.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.digitalpersona.onetouch.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

public class MainForm extends JFrame
{
	public static String TEMPLATE_PROPERTY = "template";
	private DPFPTemplate template;

	public class TemplateFileFilter extends javax.swing.filechooser.FileFilter {
		@Override public boolean accept(File f) {
			return f.getName().endsWith(".fpt");
		}
		@Override public String getDescription() {
			return "Fingerprint Template File (*.fpt)";
		}
	}
        
        
	MainForm() {
        setState(Frame.NORMAL);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Ventana de Ingreso e busqueda de Huella Digital");
		setResizable(false);

		final Button enroll = new Button("Alta Huella Digital");
                enroll.setBackground(Color.DARK_GRAY);
                enroll.setForeground(Color.WHITE);
                
        enroll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onEnroll(); }});
		
		final Button verify = new Button("Verificacion Huella Digital");
                verify.setBackground(Color.DARK_GRAY);
                verify.setForeground(Color.WHITE);
        verify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onVerify(); }});

		final Button save = new Button("Guardar Huella Digital");
                save.setBackground(Color.DARK_GRAY);
                save.setForeground(Color.WHITE);                
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onSave(); }});

		final Button load = new Button("Leer Huella Digital");
                load.setBackground(Color.DARK_GRAY);
                load.setForeground(Color.WHITE);                
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onLoad(); }});

		final Button quit = new Button("Cerrar");
                quit.setBackground(Color.DARK_GRAY);
                quit.setForeground(Color.WHITE);                
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { System.exit(0); }});
		
		this.addPropertyChangeListener(TEMPLATE_PROPERTY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				verify.setEnabled(template != null);
				save.setEnabled(template != null);
				if (evt.getNewValue() == evt.getOldValue()) return;
				if (template != null)
					JOptionPane.showMessageDialog(MainForm.this, "La Huella esta guardada, Lista para verificarse.", "Alta Huella Digital", JOptionPane.INFORMATION_MESSAGE);
			}
		});
			
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(4, 1, 0, 5));
		center.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
		center.add(enroll);
		center.add(verify);
		center.add(save);
		center.add(load);
                center.setBackground(Color.DARK_GRAY);

                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
                
                Button help = new Button("Acerca");
                bottom.add(help);
                
                help.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
           
                
                Acerca acerca = new Acerca();
                acerca.setVisible(true);
                
                
            }
        });
                
                bottom.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		
                bottom.add(quit);

                
                
		setLayout(new BorderLayout());
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.PAGE_END);
		
		pack();
		setSize((int)(getSize().width*1.6), getSize().height);
        
                setLocationRelativeTo(null);
		
                setTemplate(null);
		setVisible(true);
	}
	
	private void onEnroll() {
		EnrollmentForm form = new EnrollmentForm(this);
		form.setVisible(true);
	}

	private void onVerify() {
		VerificationForm form = new VerificationForm(this);
		form.setVisible(true);
	}

	private void onSave() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new TemplateFileFilter());
		while (true) {
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					File file = chooser.getSelectedFile();
					if (!file.toString().toLowerCase().endsWith(".fpt"))
						file = new File(file.toString() + ".fpt");
					if (file.exists()) {
						int choice = JOptionPane.showConfirmDialog(this,
							String.format("Archivos \"%1$s\" ya existe.\nUsted quiere reemplazarlo?", file.toString()),
							"Guardando Huella Digital", 
							JOptionPane.YES_NO_CANCEL_OPTION);
						if (choice == JOptionPane.NO_OPTION)
							continue;
						else if (choice == JOptionPane.CANCEL_OPTION)
							break;
					}
					FileOutputStream stream = new FileOutputStream(file);
					stream.write(getTemplate().serialize());
					stream.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Guardando Huella", JOptionPane.ERROR_MESSAGE);
				}
			}
			break;
		}
	}

	private void onLoad() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new TemplateFileFilter());
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				FileInputStream stream = new FileInputStream(chooser.getSelectedFile());
				byte[] data = new byte[stream.available()];
				stream.read(data);
				stream.close();
				DPFPTemplate t = DPFPGlobal.getTemplateFactory().createTemplate();
				t.deserialize(data);
				setTemplate(t);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Fingerprint loading", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public DPFPTemplate getTemplate() {
		return template;
	}
	public void setTemplate(DPFPTemplate template) {
		DPFPTemplate old = this.template;
		this.template = template;
		firePropertyChange(TEMPLATE_PROPERTY, old, template);
	}
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainForm();
            }
        });
    }

}
