package io.github.spaceshooter.space;

public class Bullet /*extends Sprite*/ {

    //private double speedFactor;
//
    //private SpaceShipPlayer parent;
//
    //public Bullet(GameEngine gameEngine){
    //    super(gameEngine, R.drawable.bullet);
//
    //    speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    //}
//
    //@Override
    //public void startGame() {}
//
    //@Override
    //public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    //    positionY += speedFactor * elapsedMillis;
    //    if (positionY < -height) {
    //        gameEngine.removeGameObject(this);
    //        // And return it to the pool
    //        parent.releaseBullet(this);
    //    }
    //}
//
//
    //public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY) {
    //    positionX = initPositionX - width/2;
    //    positionY = initPositionY - height/2;
    //    parent = parentPlayer;
    //}
//
    //private void removeObject(GameEngine gameEngine) {
    //    gameEngine.removeGameObject(this);
    //    // And return it to the pool
    //    parent.releaseBullet(this);
    //}
//
    //@Override
    //public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
    //    if (otherObject instanceof Asteroid) {
    //        // Remove both from the game (and return them to their pools)
    //        removeObject(gameEngine);
    //        Asteroid a = (Asteroid) otherObject;
    //        a.removeObject(gameEngine);
    //        gameEngine.onGameEvent(GameEvent.AsteroidHit);
    //        // Add some score
    //    }
    //}
}
