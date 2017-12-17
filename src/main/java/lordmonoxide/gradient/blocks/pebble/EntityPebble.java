package lordmonoxide.gradient.blocks.pebble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPebble extends EntityThrowable {
  public EntityPebble(final World world) {
    super(world);
  }
  
  public EntityPebble(final World world, final EntityLivingBase thrower) {
    super(world, thrower);
  }
  
  public EntityPebble(final World world, final double x, final double y, final double z) {
    super(world, x, y, z);
  }
  
  @Override
  protected void onImpact(final RayTraceResult result) {
    if(result.entityHit != null) {
      result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.5f);
    }
    
    if(!this.world.isRemote) {
      this.world.setEntityState(this, (byte)3);
      this.setDead();
    }
  }
}
