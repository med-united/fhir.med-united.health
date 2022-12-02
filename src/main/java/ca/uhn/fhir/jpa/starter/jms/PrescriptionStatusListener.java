package ca.uhn.fhir.jpa.starter.jms;

import java.io.StringReader;
import java.util.logging.Logger;

import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Component
public class PrescriptionStatusListener {

    private static Logger log = Logger.getLogger(PrescriptionStatusListener.class.getName());

    @Autowired
	DaoRegistry daoRegistry;

	@JmsListener(destination = "PrescriptionStatus")
	public void processMessage(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        String medicationRequestId = jsonObject.getString("medicationRequestId", "N/A");
        String info = jsonObject.getString("info", "N/A");

        log.info("Try to add note to medicationRequest: "+medicationRequestId+" info: "+info);

        IFhirResourceDao<MedicationRequest> medicationRequestDao = daoRegistry.getResourceDao(MedicationRequest.class);
        MedicationRequest medicationRequest = medicationRequestDao.read(new IdType(medicationRequestId));
        medicationRequest.addNote(new Annotation( new MarkdownType(info)));
        medicationRequestDao.update(medicationRequest);
	}

}
