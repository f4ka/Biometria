
package biemetria;


import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.verification.*;
import java.awt.*;

public class VerificationForm extends CaptureForm
{
	private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
	
	VerificationForm(Frame owner) {
		super(owner);
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Registro de Huella Digital");
		updateStatus(0);
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);

		// Procesamiento de muestra
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

		// Chequeo de muestra y comienzo de verificacion si la muestra es buena
		if (features != null)
		{
			// Compara carateristicas con la muestra
			DPFPVerificationResult result = 
				verificator.verify(features, ((MainForm)getOwner()).getTemplate());
			updateStatus(result.getFalseAcceptRate());
			if (result.isVerified())
				makeReport("La Huella Digital fue VERIFICADA.");
			else
				makeReport("La Huella Digital NO FUE VERIFICADA.");
		}
	}
	
	private void updateStatus(int FAR)
	{
		// Muestra tasa de falso positivo
		setStatus(String.format("Tasa de falso positivo (FAR) = %1$s", FAR));
	}

}
