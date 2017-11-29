/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import tools.HardCodedParameters;
import tools.User;
import tools.Position;
import tools.Sound;

import specifications.EngineService;
import specifications.DataService;
import specifications.RequireDataService;
import specifications.PhantomService;
import specifications.EnnemieService;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Random;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService{
    private static final double friction=HardCodedParameters.friction,
            heroesStep=HardCodedParameters.heroesStep,
                    phantomStep=HardCodedParameters.phantomStep,
                    ennemieStep=HardCodedParameters.phantomStep;
    private Timer engineClock;
    private DataService data;
    private User.COMMAND command;
    private Random gen;
    private boolean moveLeft,moveRight,moveUp,moveDown;
    private double heroesVX,heroesVY;
    private double vitesse;
    private MediaPlayer bgSound;
    private boolean paused;
    int score=0;
    int iteneration=0;
    int niv=0;
 
    public Engine(){}
 
    @Override
    public void bindDataService(DataService service){
        data=service;
    }
 
    @Override
    public void init(){
        //engineClock = new Timer();
        command = User.COMMAND.NONE;
        gen = new Random();
        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;
        heroesVX = 0;
        heroesVY = 0;
        vitesse = 0.1;
        paused = false;
        score=0;
        iteneration=0;
        niv=0;
 
        bgSound = new MediaPlayer(new Media(new File("src/sound/furElise.mp3").toURI().toString()));
        bgSound.setOnEndOfMedia(new Runnable() {
            public void run() {
                bgSound.seek(Duration.ZERO);
            }
        });
    }
 
    @Override
    public void start(){
        bgSound.play();
        engineClock = new Timer();
        engineClock.schedule(new TimerTask(){
            public void run() {
            	final URL resource = getClass().getResource("/sound/waterdrip.wav");
            	  final Media media = new Media(resource.toString());
            	  final MediaPlayer mediaPlayer = new MediaPlayer(media);
                if (gen.nextInt(60)<3) spawnPhantom();
                if (gen.nextInt(60)<3) spawnEnnemie();
 
                updateSpeedHeroes();
                updateCommandHeroes();
                updatePositionHeroes();
                score=0;
                ArrayList<PhantomService> phantoms = new ArrayList<PhantomService>();
                ArrayList<EnnemieService> ennemie = new ArrayList<EnnemieService>();
 
                data.setSoundEffect(Sound.SOUND.None);
 
                for (PhantomService p:data.getPhantoms()){
                    if (p.getAction()==PhantomService.MOVE.LEFT) moveLeft(p);
                    if (p.getAction()==PhantomService.MOVE.RIGHT) moveRight(p);
                    if (p.getAction()==PhantomService.MOVE.UP) moveUp(p);
                    if (p.getAction()==PhantomService.MOVE.DOWN) moveDown(p);
 
                    if (p.getPosition().x <= 0){
                        p.setPosition(new Position (phantomStep, p.getPosition().y));
                    }
                    if (collisionHeroesPhantom(p)){
                        //data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                  	  mediaPlayer.play();
                        score++;
                        iteneration++;
                        if(iteneration==10){
                        	iteneration = 0;

                        	vie_plus();
                        	HardCodedParameters.niv++;
                        	stop();
                        	HardCodedParameters.enginePaceMillis=HardCodedParameters.enginePaceMillis/3;
                        	start();
                        }
                    }
                    else {
                        if(collisionWallPhantom(p)){
                        }
                        else if (p.getPosition().y<800) phantoms.add(p);
                    }
                }
                for (EnnemieService e:data.getEnnemie()){
                    if (e.getAction()==EnnemieService.MOVE.LEFT) moveLeft(e);
                    if (e.getAction()==EnnemieService.MOVE.RIGHT) moveRight(e);
                    if (e.getAction()==EnnemieService.MOVE.UP) moveUp(e);
                    if (e.getAction()==EnnemieService.MOVE.DOWN) moveDown(e);
 
                    if (e.getPosition().x <= 0){
                        e.setPosition(new Position (phantomStep, e.getPosition().y));
                    }
                    if (collisionHeroesEnnemie(e)){
                        data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                        score++;
                        iteneration++;
                        if(iteneration==10){
                        	iteneration = 0;
                        	HardCodedParameters.niv++;
                        	stop();
                        	HardCodedParameters.enginePaceMillis=HardCodedParameters.enginePaceMillis/2;
                        	start();
                        }
                    }
                    else {
                        if(collisionWallEnnemie(e)){
                        }
                        else if (e.getPosition().y<800) ennemie.add(e);
                    }
                }
 
                data.addScore(score);
                data.setPhantoms(phantoms);
 
                data.setStepNumber(data.getStepNumber()+1);
            }
        },0,HardCodedParameters.enginePaceMillis);
    }
 
    @Override
    public void stop(){
        bgSound.pause();
        engineClock.cancel();
    }
 
    @Override
    public void setHeroesCommand(User.COMMAND c){
        if (c==User.COMMAND.LEFT) moveLeft=true;
        if (c==User.COMMAND.RIGHT) moveRight=true;
        if (c==User.COMMAND.UP) moveUp=true;
        if (c==User.COMMAND.DOWN) moveDown=true;
    }
 
    @Override
    public void releaseHeroesCommand(User.COMMAND c){
        if (c==User.COMMAND.LEFT) moveLeft=false;
        if (c==User.COMMAND.RIGHT) moveRight=false;
        if (c==User.COMMAND.UP) moveUp=false;
        if (c==User.COMMAND.DOWN) moveDown=false;
    }
 
    private void updateSpeedHeroes(){
        heroesVX*=friction;
        heroesVY*=friction;
    }
 
    private void updateCommandHeroes(){
        if (moveLeft) heroesVX-=heroesStep;
        if (moveRight) heroesVX+=heroesStep;
        if (moveUp) heroesVY-=heroesStep;
        if (moveDown) heroesVY+=heroesStep;
    }
 
    private void updatePositionHeroes(){
        Position newPos = new Position(data.getHeroesPosition().x+heroesVX,data.getHeroesPosition().y+heroesVY);
 
        if (newPos.x<0) newPos.x = heroesStep*2;
        if (newPos.y<0) newPos.y = heroesStep*2;
        if (newPos.x>HardCodedParameters.defaultWidth) newPos.x -= heroesStep*2;
        if (newPos.y>450) newPos.y -= heroesStep*2;
 
        data.setHeroesPosition(newPos);
    }
 
    private void spawnPhantom(){
        int y=0;
        int x=(int)(HardCodedParameters.defaultHeight*.9);
        boolean cont=true;
        while (cont) {
            x=(int)(gen.nextInt((int)(HardCodedParameters.defaultWidth*.6))+HardCodedParameters.defaultWidth*.1);
            cont=false;
            for (PhantomService p:data.getPhantoms()){
                if (p.getPosition().equals(new Position(x,y))) cont=true;
            }
        }
        data.addPhantom(new Position(x,y));
    }
    private void spawnEnnemie(){
        int y=0;
        int x=(int)(HardCodedParameters.defaultHeight*.9);
        boolean cont=true;
        while (cont) {
            x=(int)(gen.nextInt((int)(HardCodedParameters.defaultWidth*.6))+HardCodedParameters.defaultWidth*.1);
            cont=false;
            for (EnnemieService e:data.getEnnemie()){
                if (e.getPosition().equals(new Position(x,y))) cont=true;
            }
        }
        data.addEnnemie(new Position(x,y));
    }
 
    private void moveLeft(PhantomService p){
        p.setPosition(new Position(p.getPosition().x-phantomStep,p.getPosition().y));
    }
 
    private void moveRight(PhantomService p){
        p.setPosition(new Position(p.getPosition().x+phantomStep,p.getPosition().y));
    }
 
    private void moveUp(PhantomService p){
        p.setPosition(new Position(p.getPosition().x,p.getPosition().y-phantomStep));
    }
 
    private void moveDown(PhantomService p){
        p.setPosition(new Position(p.getPosition().x,p.getPosition().y+phantomStep));
    }
 
    private void moveLeft(EnnemieService e){
        e.setPosition(new Position(e.getPosition().x-phantomStep,e.getPosition().y));
    }
    private void vie_plus(){
  	  data.setHeroesPointVie(data.getHeroesPointVie()+1);
    }
    private void moveRight(EnnemieService e){
        e.setPosition(new Position(e.getPosition().x+phantomStep,e.getPosition().y));
    }
 
    private void moveUp(EnnemieService e){
        e.setPosition(new Position(e.getPosition().x,e.getPosition().y-phantomStep));
    }
 
    private void moveDown(EnnemieService e){
        e.setPosition(new Position(e.getPosition().x,e.getPosition().y+phantomStep));
    }
    private boolean collisionHeroesPhantom(PhantomService p){
        boolean retour = false;
        if (p.getPosition().compareX(data.getHeroesPosition()) > - 25
                && p.getPosition().compareX(data.getHeroesPosition()) < 25
                && p.getPosition().compareY(data.getHeroesPosition()) > - 65    
                && p.getPosition().compareY(data.getHeroesPosition()) < 50)     
            retour = true;
 
 
        return retour;
    }
    
    private boolean collisionHeroesEnnemie(EnnemieService e){
        boolean retour = false;
        if (e.getPosition().compareX(data.getHeroesPosition()) > - 25
                && e.getPosition().compareX(data.getHeroesPosition()) < 25
                && e.getPosition().compareY(data.getHeroesPosition()) > - 65    
                && e.getPosition().compareY(data.getHeroesPosition()) < 50)     
            retour = true;
 
 
        return retour;
    }
    
    
    private boolean collisionWallPhantom(PhantomService p){
        boolean retour = false;
        if (p.getPosition().y>480)     
            retour = true;
 
 
        return retour;
    }
    
    private boolean collisionWallEnnemie(EnnemieService e){
        boolean retour = false;
        if (e.getPosition().x < 40)     
            retour = true;
 
 
        return retour;
    }
 
    private boolean collisionHeroesPhantoms(){
        for (PhantomService p:data.getPhantoms()) if (collisionHeroesPhantom(p)) return true; return false;
    }
 
    @Override
    public void pause() {
        paused = !paused;
 
        if (paused){
            stop();
        }else start();
    }
}