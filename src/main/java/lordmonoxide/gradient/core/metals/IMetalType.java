package lordmonoxide.gradient.core.metals;

public interface IMetalType {
  String getName();
  int    getMeltTime();
  float  getMeltTemp();
  float  getHardness();
  float  getWeight();

  int getColourDiffuse();
  int getColourSpecular();
  int getColourShadow1();
  int getColourShadow2();
  int getColourEdge1();
  int getColourEdge2();
  int getColourEdge3();
}
