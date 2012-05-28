package nuclearcontrol;

import ic2.api.INetworkClientTileEntityEventListener;
import ic2.api.INetworkDataProvider;
import ic2.api.INetworkUpdateListener;
import ic2.api.IWrenchable;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TileEntityIC2Thermo extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, INetworkClientTileEntityEventListener, IWrenchable {

   private boolean init;
   private int prevHeatLevel;
   public int heatLevel;
   private int mappedHeatLevel;
   private byte prevOnFire;
   public byte onFire;
   private short prevFacing;
   public short facing;
   private int updateTicker;
   private int tickRate;


   public TileEntityIC2Thermo() {
      // $FF: Couldn't be decompiled
   }

   public void initData() {
      // $FF: Couldn't be decompiled
   }

   public short getFacing() {
      // $FF: Couldn't be decompiled
   }

   public void setFacing(short param1) {
      // $FF: Couldn't be decompiled
   }

   public List getNetworkedFields() {
      // $FF: Couldn't be decompiled
   }

   public void onNetworkUpdate(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void onNetworkEvent(EntityHuman param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void setOnFire(byte param1) {
      // $FF: Couldn't be decompiled
   }

   public byte getOnFire() {
      // $FF: Couldn't be decompiled
   }

   public void setHeatLevel(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void setHeatLevelWithoutNotify(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int getHeatLevel() {
      // $FF: Couldn't be decompiled
   }

   public void a(NBTTagCompound param1) {
      // $FF: Couldn't be decompiled
   }

   public void b(NBTTagCompound param1) {
      // $FF: Couldn't be decompiled
   }

   private void checkStatus() {
      // $FF: Couldn't be decompiled
   }

   public void q_() {
      // $FF: Couldn't be decompiled
   }

   public boolean wrenchCanSetFacing(EntityHuman param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public boolean wrenchCanRemove(EntityHuman param1) {
      // $FF: Couldn't be decompiled
   }

   public float getWrenchDropRate() {
      // $FF: Couldn't be decompiled
   }
}
