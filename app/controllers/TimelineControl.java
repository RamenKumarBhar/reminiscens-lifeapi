package controllers;

import models.User;
import be.objectify.deadbolt.java.actions.Dynamic;
import delegates.TimelineDelegate;
import delegates.UtilitiesDelegate;
import enums.LogActions;
import enums.ResponseStatus;
import play.Play;
import play.data.Form;
import play.mvc.*;
import pojos.ResponseStatusBean;
import pojos.TimelineBean;
import security.SecurityModelConstants;
import static play.libs.Json.toJson;


public class TimelineControl extends Controller {
	static Form<TimelineBean> timelineForm = Form.form(TimelineBean.class);
	
	@Dynamic(value="FriendOf", meta=SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPersonTimeline(Long id) {
		TimelineBean bean = TimelineDelegate.getInstance().getTimeline(id);
		if (bean!=null) {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");	
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user, LogActions.TIMELINE_READ.toString(), request().path());
			}
			return ok(toJson(bean));
		} else {
			return notFound();
		}
	}
	
	@Dynamic(value="FriendOf", meta=SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPersonTimelineWithLimits(Long id, Long from, Long to) {
		TimelineBean bean = TimelineDelegate.getInstance().getTimelineWithLimits(id, from, to);
		if (bean!=null) {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");	
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user, LogActions.TIMELINE_READ.toString(), request().path());
			}
			return ok(toJson(bean));
		} else {
			return notFound();
		}
	}
	
	@Dynamic(value="FriendOf", meta=SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPersonTimelineByDecade(Long id, Long decade) {
		TimelineBean bean = TimelineDelegate.getInstance().getTimelineByDecade(id, decade);
		if (bean!=null) {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");	
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user, LogActions.TIMELINE_READ.toString(), request().path());
			}
			return ok(toJson(bean));
		} else {
			return notFound();
		}
	}

	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_PERSON)
	public static Result synchronizePersonTimeline(Long id) {
		Form<TimelineBean> filledForm = timelineForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");	
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user, LogActions.TIMELINE_SYNC.toString(), request().path());
			}
			
			TimelineBean timelineBean = filledForm.get();
			TimelineDelegate.getInstance().synchronize(timelineBean);
			return ok(toJson(timelineBean));
		}
	}
	
	/*
	 * TODO
	 * 1. Verify Timeline Synchronize
	 * 2. Implement a synchronize methods that exchange only IDs of stories 
	 * 	  and stories that haven't been synced yet
	 */
}
