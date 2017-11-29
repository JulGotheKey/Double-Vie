/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;
import tools.Sound;

import specifications.DataService;
import specifications.EnnemieService;
import specifications.PhantomService;

import data.ia.MoveLeftPhantom;

import java.util.ArrayList;

public class Data implements DataService{
  //private Heroes hercules;
  private Position heroesPosition;
  private int stepNumber, score, niv;
  int heroesPointVie;
  private ArrayList<PhantomService> phantoms;
  private ArrayList<EnnemieService> ennemie;
  private double heroesWidth,heroesHeight,phantomWidth,phantomHeight,ennemieWidth,ennemieHeight;
  private Sound.SOUND sound;

  public Data(){}

  @Override
  public void init(){
    //hercules = new Heroes;
    heroesPosition = new Position(HardCodedParameters.heroesStartX,HardCodedParameters.heroesStartY);
    phantoms = new ArrayList<PhantomService>();
    ennemie = new ArrayList<EnnemieService>();
    stepNumber = 0;
    score = 0;
    heroesPointVie = 3;
    niv= 1;
    heroesWidth = HardCodedParameters.heroesWidth;
    heroesHeight = HardCodedParameters.heroesHeight;
    phantomWidth = HardCodedParameters.phantomWidth;
    phantomHeight = HardCodedParameters.phantomHeight;
    ennemieWidth = HardCodedParameters.ennemieWidth;
    ennemieHeight = HardCodedParameters.ennemieHeight;
    sound = Sound.SOUND.None;
  }

  @Override
  public Position getHeroesPosition(){ return heroesPosition; }
  
  @Override
  public double getHeroesWidth(){ return heroesWidth; }
  
  @Override
  public double getHeroesHeight(){ return heroesHeight; }
  
  @Override
  public double getPhantomWidth(){ return phantomWidth; }
  
  @Override
  public double getPhantomHeight(){ return phantomHeight; }

  @Override
  public int getStepNumber(){ return stepNumber; }
  
  @Override
  public int getScore(){ return score; }

  @Override
  public ArrayList<PhantomService> getPhantoms(){ return phantoms; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }

  @Override
  public void setHeroesPosition(Position p) { heroesPosition=p; }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }
  
  @Override
  public void addScore(int score){ this.score+=score; }
  public void addNiv(int niv){ this.niv+=niv; }

  @Override
  public void addPhantom(Position p) { phantoms.add(new MoveLeftPhantom(p)); }
  
  @Override
  public void setPhantoms(ArrayList<PhantomService> phantoms) { this.phantoms=phantoms; }
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }

  
  
  
  
  
  
@Override
public double getEnnemieWidth() {
	// TODO Auto-generated method stub
	return ennemieWidth;
}

@Override
public double getEnnemieHeight() {
	// TODO Auto-generated method stub
	return ennemieHeight;
}

@Override
public ArrayList<EnnemieService> getEnnemie() {
	// TODO Auto-generated method stub
	return ennemie;
}

@Override
public void addEnnemie(Position p) {
	phantoms.add(new MoveLeftPhantom(p));
	
}

@Override
public void setEnnemie(ArrayList<EnnemieService> ennemie) {
	this.ennemie=ennemie;
	
}

@Override
public int getHeroesPointVie() {
	return heroesPointVie;
}

@Override
public int setHeroesPointVie(int i) {
	return heroesPointVie = i;
}
}
