package com.xmog.web.websocket;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.xmog.stack.templating.json.JsonMapper;
import com.xmog.stack.web.websocket.StackWebSocket;

/**
 * @author Let's Play Internets LLC.
 */
@Path("/websocket/game/start")
public class GameStartWebSocket extends StackWebSocket {
//  private Long gameId;
//  private Long ownerId;
//
//  @Inject
//  private OwnerService ownerService;
//
//  @Inject
//  private GameService gameService;
//
//  @Inject
//  private JsonMapper jsonMapper;
//
//  private EventBus eventBus;
//
//  private Logger logger = getLogger(getClass());
//
//  @Inject
//  public GameStartWebSocket(EventBus eventBus) {
//    super();
//    eventBus.register(this);
//    this.eventBus = eventBus;
//  }
//
//  @Override
//  protected void onClose(int statusCode, String reason) {
//    getEventBus().unregister(this);
//  }
//
//  @Subscribe
//  public void handleGameStartedEvent(GameStartedEvent event) {
//    // Ignore events if they're for games we don't care about or if our socket is closed
//    if (gameId == null || !gameId.equals(event.getGameId()) || isNotConnected())
//      return;
//
//    logger.debug(format("%s received %s. Letting client know the game has started...", this, event));
//
//    Game game = gameService.findGameById(gameId);
//
//    if (game == null) {
//      logger.debug(format("Unable to find game ID %s.", gameId));
//      return;
//    }
//
//    getRemote().sendStringByFuture(jsonMapper.toJson(gameService.extractCompleteGameData(game)));
//  }
//
//  @Override
//  protected void onMessage(String json) {
//    GameStartListenerCommand command = jsonMapper.fromJson(json, GameStartListenerCommand.class);
//
//    if (command.getGameId() == null) {
//      logger.debug("No game ID specified.");
//      return;
//    }
//
//    Owner owner = ownerService.findOwnerByApiToken(command.getOwnerApiToken());
//
//    if (owner == null) {
//      logger.debug(format("No owner account found for API token '%s'.", command.getOwnerApiToken()));
//      return;
//    }
//
//    Game game = gameService.findGameById(command.getGameId());
//
//    if (game == null) {
//      logger.debug(format("No game found for game ID %s.", command.getGameId()));
//      return;
//    }
//
//    if (!game.getOwnerId().equals(owner.getOwnerId())) {
//      logger.debug(format("Game ID %s is not owned by owner ID %s.", gameId, ownerId));
//      return;
//    }
//
//    this.ownerId = owner.getOwnerId();
//    this.gameId = command.getGameId();
//  }
//
//  public EventBus getEventBus() {
//    return eventBus;
//  }
//
//  static class GameStartListenerCommand {
//    private String ownerApiToken;
//    private Long gameId;
//
//    public String getOwnerApiToken() {
//      return ownerApiToken;
//    }
//
//    public void setOwnerApiToken(String ownerApiToken) {
//      this.ownerApiToken = ownerApiToken;
//    }
//
//    public Long getGameId() {
//      return gameId;
//    }
//
//    public void setGameId(Long gameId) {
//      this.gameId = gameId;
//    }
//  }
}