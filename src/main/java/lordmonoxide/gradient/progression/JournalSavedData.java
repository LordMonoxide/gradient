package lordmonoxide.gradient.progression;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class JournalSavedData extends WorldSavedData {
  private static final String DATA_NAME = GradientMod.MODID + "_journal";
  
  public static JournalSavedData get(World world) {
    MapStorage storage = world.getMapStorage();
    
    JournalSavedData instance = (JournalSavedData)storage.getOrLoadData(JournalSavedData.class, DATA_NAME);
    
    if(instance == null) {
      instance = new JournalSavedData();
      storage.setData(DATA_NAME, instance);
    }
    
    return instance;
  }
  
  public JournalSavedData() {
    super(DATA_NAME);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    
  }
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    return null;
  }
}
