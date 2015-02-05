
package biemetria;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.*;
import java.awt.*;
import javax.swing.JOptionPane;

public class EnrollmentForm extends CaptureForm
{
	private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
	
	EnrollmentForm(Frame owner) {
		super(owner);
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Registro de Huellas");
		updateStatus();
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);
		// Procesamiento de muestra
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

		// Chequeo de la calidad de la muestra
		if (features != null) try
		{
			makeReport("Muestra de huella digital creada.");
			enroller.addFeatures(features);		// Addicion de muestra
		}
		catch (DPFPImageQualityException ex) { }
		finally {
			updateStatus();

			// Chequeo si la muestra fue creada.
			switch(enroller.getTemplateStatus())
			{
				case TEMPLATE_STATUS_READY:	// Reportar exito y parar captura
					stop();
					((MainForm)getOwner()).setTemplate(enroller.getTemplate());
					setPrompt("Click en Cerrar, y entonces click en verificacion de huella.");
					break;

				case TEMPLATE_STATUS_FAILED:	// Reportar fallo y reiniciar captura
					enroller.clear();
					stop();
					updateStatus();
					((MainForm)getOwner()).setTemplate(null);
					JOptionPane.showMessageDialog(EnrollmentForm.this, "La muestra de huella digital no es valida, intente volver a registrar la huella.", "Registro de Huella Digital", JOptionPane.ERROR_MESSAGE);
					start();
					break;
			}
		}
	}
	
	private void updateStatus()
	{
		// Muestra el numero de muestras necesarias
		setStatus(String.format("Muestras de Huella necesarias: %1$s", enroller.getFeaturesNeeded()));
	}
	
}
