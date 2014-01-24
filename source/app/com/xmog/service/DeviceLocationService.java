package com.xmog.service;

import static com.google.common.base.Preconditions.checkNotNull;
//import static com.letsplayinternets.model.QuestionType.QUESTION_TYPE_AUTOCOMPLETE_THIS;
//import static com.letsplayinternets.model.QuestionType.QUESTION_TYPE_NAME_THAT_MEME;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trimToEmpty;
import static org.apache.commons.lang.StringUtils.trimToNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.command.SaveLocationCommand;
import com.xmog.event.LocationUpdateEvent;
import com.xmog.model.DeviceLocation;
//import com.letsplayinternets.command.AnswerCreateCommand;
//import com.letsplayinternets.command.GameCreateCommand;
//import com.letsplayinternets.command.QuestionCreateCommand;
//import com.letsplayinternets.event.GameStartedEvent;
//import com.letsplayinternets.event.QuestionAnsweredEvent;
//import com.letsplayinternets.model.Answer;
//import com.letsplayinternets.model.Game;
//import com.letsplayinternets.model.Question;
//import com.letsplayinternets.model.QuestionType;
import com.xmog.stack.db.Database;
import com.xmog.stack.exception.AuthenticationException;
import com.xmog.stack.exception.NotFoundException;
import com.xmog.stack.exception.ValidationException;
import com.xmog.stack.exception.ValidationException.FieldError;

/**
 * @author Transmogrify, LLC.
 */
@Singleton
public class DeviceLocationService {
  @Inject
  private Database database;

  @Inject
  private EventBus eventBus;

  private static final Logger logger = getLogger(DeviceLocationService.class);

  public void saveDeviceLocation(SaveLocationCommand saveLocationCommand) {
    checkNotNull(saveLocationCommand);

    Long deviceLocationId = database.executeInsert("INSERT into device_location(device_location_id, device_identifier, speed, course, latitude, longitude, accuracy) " + 
    "values(nextval('device_location_seq'), ?,?,?,?,?,?)  RETURNING device_location_id",Long.class,saveLocationCommand.getDeviceIdentifier(), saveLocationCommand.getSpeed(), saveLocationCommand.getCourse(), saveLocationCommand.getLatitude()
    ,saveLocationCommand.getLongitude(), saveLocationCommand.getAccuracy());
    
    eventBus.post(new LocationUpdateEvent(deviceLocationId));
    logger.debug(format("Saved a location and sending an event to %s", eventBus));
  }

  public DeviceLocation findDeviceLocationById(Long deviceLocationId) {
    if (deviceLocationId == null)
      return null;

    return database.queryForObject("SELECT * FROM device_location WHERE device_location_id=?", DeviceLocation.class, deviceLocationId);
  }

//  public Map<String, Object> extractCompleteGameData(Game game) {
//    checkNotNull(game);
//
//    Map<String, Object> data = new HashMap<String, Object>();
//    List<Map<String, Object>> questionObjects = new ArrayList<Map<String, Object>>();
//
//    long dateStartedInSeconds = game.getDateStarted().getTime() / 1000L;
//    long currentDateInSeconds = new Date().getTime() / 1000L;
//    long secondsSinceGameStarted = currentDateInSeconds - dateStartedInSeconds;
//    long secondsUntilNextQuestion = 0L;
//    long secondsForQuestions = 0L;
//
//    List<Question> questions = findQuestionsByGameId(game.getGameId());
//
//    for (Question question : questions) {
//      secondsForQuestions += question.getSecondsToAnswer() + game.getSecondsBetweenQuestions();
//
//      if (secondsSinceGameStarted > secondsForQuestions)
//        continue;
//
//      if (questionObjects.size() == 0)
//        secondsUntilNextQuestion = secondsForQuestions - secondsSinceGameStarted;
//
//      Map<String, Object> questionObject = new HashMap<String, Object>();
//
//      questionObject.put("questionId", question.getQuestionId());
//      questionObject.put("questionTypeId", question.getQuestionTypeId());
//
//      long secondsToAnswer = question.getSecondsToAnswer();
//
//      // First question, we allow you to answer if you jump in midway (within reason)
//      if (questionObjects.size() == 0)
//        secondsToAnswer = secondsUntilNextQuestion - game.getSecondsBetweenQuestions();
//
//      questionObject.put("secondsToAnswer", secondsToAnswer);
//
//      if (question.getQuestion() != null)
//        questionObject.put("question", question.getQuestion());
//
//      if (question.getImageUrl() != null)
//        questionObject.put("imageUrl", question.getImageUrl());
//
//      List<Map<String, Object>> answers = new ArrayList<Map<String, Object>>();
//
//      for (final Answer answer : findAnswersByQuestionId(question.getQuestionId()))
//        answers.add(new HashMap<String, Object>() {
//          {
//            put("answerId", answer.getAnswerId());
//            put("correct", answer.getCorrect());
//
//            if (answer.getAnswer() != null)
//              put("answer", answer.getAnswer());
//
//            if (answer.getImageUrl() != null)
//              put("imageUrl", answer.getImageUrl());
//          }
//        });
//
//      questionObject.put("answers", answers);
//
//      questionObjects.add(questionObject);
//    }
//
//    if (questionObjects.size() == 1 || secondsUntilNextQuestion > game.getSecondsBetweenQuestions() + 5)
//      secondsUntilNextQuestion = 0;
//
//    if (secondsUntilNextQuestion != 0)
//      questionObjects.remove(0);
//
//    data.put("name", game.getName());
//    data.put("urlPath", game.getUrlPath());
//    data.put("questions", questionObjects);
//    data.put("secondsBetweenQuestions", game.getSecondsBetweenQuestions());
//    data.put("secondsUntilNextQuestion", secondsUntilNextQuestion);
//    data.put("secondsSinceGameStarted", secondsSinceGameStarted);
//
//    return data;
//  }

}