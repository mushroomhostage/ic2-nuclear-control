package nuclearcontrol;

import ic2.api.IElectricItem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import nuclearcontrol.ItemToolThermometer;
import nuclearcontrol.ThermometerVersion;

public class ItemToolDigitalThermometer extends ItemToolThermometer implements IElectricItem {

   public int tier;
   public int ratio;
   public int transfer;


   public ItemToolDigitalThermometer(int param1, int param2, ThermometerVersion param3, int param4, int param5, int param6) {
      // $FF: Couldn't be decompiled
   }

   public boolean canTakeDamage(ItemStack param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void damage(ItemStack param1, int param2, EntityHuman param3) {
      // $FF: Couldn't be decompiled
   }

   public boolean canProvideEnergy() {
      // $FF: Couldn't be decompiled
   }

   public int getChargedItemId() {
      // $FF: Couldn't be decompiled
   }

   public int getEmptyItemId() {
      // $FF: Couldn't be decompiled
   }

   public int getMaxCharge() {
      // $FF: Couldn't be decompiled
   }

   public int getTier() {
      // $FF: Couldn't be decompiled
   }

   public int getTransferLimit() {
      // $FF: Couldn't be decompiled
   }
}
