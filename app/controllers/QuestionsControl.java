package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import models.User;
import delegates.QuestionDelegate;
import delegates.UtilitiesDelegate;
import enums.LogActions;
import play.Play;
import play.mvc.*;
import pojos.QuestionBean;

public class QuestionsControl extends Controller {

    public static Result getGeneralQuestionsForYear(Integer byear, Integer fyear) {
		@SuppressWarnings("static-access")
		List<QuestionBean> lp = QuestionDelegate.getInstance().getQuestionsForLifeDecade(byear,fyear);
		if (lp!=null) {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user,
						LogActions.QUESTION_READ.toString(), request().path());
			}
		}
		return lp != null ? ok(toJson(lp)) : notFound();
	}

	public static Result getGeneralQuestionsForLifeChapter(String chapter) {
		@SuppressWarnings("static-access")
		List<QuestionBean> lp = QuestionDelegate.getInstance().getQuestionsForLifeChapter(chapter);
		if (lp!=null) {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user,
						LogActions.QUESTION_READ.toString(), request().path());
			}
		}
		return lp != null ? ok(toJson(lp)) : notFound();
	}
	
	public static Result addQuestionToContext(Long contextId) {
		return TODO;
	}
	
	public static Result getQuestionById(Long questionId) {@SuppressWarnings("static-access")
	QuestionBean question = QuestionDelegate.getInstance().getQuestion(questionId);
		if (question!=null) {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			// Log Action
			int logAction = Play.application().configuration()
					.getInt("log.actions");
			if (logAction == 1) {
				UtilitiesDelegate.getInstance().logActivity(user,
						LogActions.QUESTION_READ.toString(), request().path());
			}
		}
		return question != null ? ok(toJson(question)) : notFound();
	}
}
