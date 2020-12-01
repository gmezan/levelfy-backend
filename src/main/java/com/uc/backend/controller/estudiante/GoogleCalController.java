package com.uc.backend.controller.estudiante;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
public class GoogleCalController {

	private final static Log logger = LogFactory.getLog(GoogleCalController.class);
	private static final String APPLICATION_NAME = "";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;

	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;

	@Value("${google.client.client-id}")
	private String clientId;
	@Value("${google.client.client-secret}")
	private String clientSecret;
	@Value("${google.client.redirectUri}")
	private String redirectURI;

	private Set<Event> events = new HashSet<>();

	final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
	final DateTime date2 = new DateTime(new Date());

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@RequestMapping(value = "/asesoriaOnline/agregaralcalendario", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		return new RedirectView(authorize());
	}

	/*
	@RequestMapping(value = "/asesoriaOnline/agregaralcalendario", method = RequestMethod.GET, params = "code")
	public String oauth2Callback(@RequestParam(value = "code") String code, RedirectAttributes redirectAttributes, HttpSession session) throws IOException {
		com.google.api.services.calendar.model.Events eventList;
		String message;
		AsesoriaEnroll asesoriaEnroll= (AsesoriaEnroll) session.getAttribute("asesoriaEnroll");
		Asesoria asesoria = (Asesoria) session.getAttribute("asesoria");
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<ClaseSesion> claseSesion = (List<ClaseSesion>) session.getAttribute("paqueteAsesoriaSesion");
		PaqueteAsesoria paqueteAsesoria=  (PaqueteAsesoria) session.getAttribute("paqueteAsesoria");
		try {
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
			Events events = client.events();
			eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
			message = eventList.getItems().toString();
			System.out.println("My:" + eventList.getItems());
		} catch (Exception e) {
			logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.");
			message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}
		boolean isPaquete= (boolean) session.getAttribute("isPaquete");
		if (!isPaquete) {
			Event event = new Event()
					.setSummary(asesoria.getCurso().getNombre())
					.setLocation("PUCP")
					.setDescription( asesoria.getCurso().getDescripcion()+" sesión zoom: "+asesoria.getZoom());


			LocalDateTime as = asesoriaEnroll.getDatetime_asesoria();
			Instant instant = as.toInstant(ZoneOffset.ofHours(-5));


			Date startDate = Date.from(instant);


			DateTime startDateTime = new DateTime(startDate);
			EventDateTime start = new EventDateTime()
					.setDateTime(startDateTime)
					.setTimeZone("America/Lima");
			event.setStart(start);

			DateTime endDateTime = new DateTime(startDate);
			EventDateTime end = new EventDateTime()
					.setDateTime(startDateTime)
					.setTimeZone("America/Lima");
			event.setEnd(end);
			EventAttendee[] attendees = new EventAttendee[] {
					new EventAttendee().setEmail("peru.universityclass@gmail.com"),
			};
			event.setAttendees(Arrays.asList(attendees));

			EventReminder[] reminderOverrides = new EventReminder[]{
					new EventReminder().setMethod("email").setMinutes(24 * 60),
					new EventReminder().setMethod("popup").setMinutes(10),
			};
			Event.Reminders reminders = new Event.Reminders()
					.setUseDefault(false)
					.setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);

			String calendarId = "primary";
			event = client.events().insert(calendarId, event).execute();
			System.out.printf("Event created: %s\n", event.getHtmlLink());


			System.out.println("cal message:" + message);

			redirectAttributes.addFlashAttribute("msg", "¡Genial! Tu asesoría se agregó a tu calendario exitosamente. Se te enviará un correo un día antes con lo necesario y un recordatorio 10 min. antes");
			return "redirect:/u/misCursos";
		}
		else{

			for (ClaseSesion claseSesion1 : claseSesion) {
				Event event = new Event()
						.setSummary(paqueteAsesoria.getCurso().getNombre())
						.setLocation("PUCP")
						.setDescription(paqueteAsesoria.getCurso().getDescripcion() +" sesion zoom:"+ claseSesion1.getZoom() );


				LocalDateTime as = claseSesion1.getFechahora();
				Instant instant = as.toInstant(ZoneOffset.ofHours(-5));
				;
				Date startDate = Date.from(instant);
				DateTime startDateTime = new DateTime(startDate);


				EventDateTime start = new EventDateTime()
						.setDateTime(startDateTime)
						.setTimeZone("America/Lima");
				event.setStart(start);

				DateTime endDateTime = new DateTime(startDate);
				EventDateTime end = new EventDateTime()
						.setDateTime(endDateTime)
						.setTimeZone("America/Lima");
				event.setEnd(end);
				EventAttendee[] attendees = new EventAttendee[] {
						new EventAttendee().setEmail("peru.universityclass@gmail.com"),
				};
				event.setAttendees(Arrays.asList(attendees));

				EventReminder[] reminderOverrides = new EventReminder[]{
						new EventReminder().setMethod("email").setMinutes(24 * 60),
						new EventReminder().setMethod("popup").setMinutes(10),
				};
				Event.Reminders reminders = new Event.Reminders()
						.setUseDefault(false)
						.setOverrides(Arrays.asList(reminderOverrides));
				event.setReminders(reminders);

				String calendarId = "primary";
				event = client.events().insert(calendarId, event).execute();
				System.out.printf("Event created: %s\n", event.getHtmlLink());
			}

			System.out.println("cal message:" + message);


			redirectAttributes.addFlashAttribute("msg","¡Genial! Tu asesoría se agregó a tu calendario exitosamente. Se te enviará un correo un día antes con lo necesario y un recordatorio 10 min. antes");
			return "redirect:/u/misCursos";
}
	}*/

	public Set<Event> getEvents() throws IOException {
		return this.events;
	}





	private String authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
		System.out.println("cal authorizationUrl->" + authorizationUrl);
		return authorizationUrl.build();
	}






}