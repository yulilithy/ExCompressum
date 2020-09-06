package net.blay09.mods.excompressum.compat.top;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockAutoSieveBase;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.WoodenCrucibleBlock;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.tile.BaitTileEntity;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class TheOneProbeAddon implements Function<ITheOneProbe, Void> {

	@Nullable
	@Override
	public Void apply(@Nullable ITheOneProbe top) {
		if(top != null) {
			top.registerProvider(new ProbeInfoProvider());
		}
		return null;
	}

	public static class ProbeInfoProvider implements IProbeInfoProvider {
		@Override
		public String getID() {
			return ExCompressum.MOD_ID;
		}

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, BlockState state, IProbeHitData data) {
			// NOTE It seems like TheOneProbe does not have localization support o_O
			if(state.getBlock() instanceof BlockAutoSieveBase) {
				AutoSieveTileEntityBase tileEntity = tryGetTileEntity(world, data.getPos(), AutoSieveTileEntityBase.class);
				if(tileEntity != null) {
					addAutoSieveInfo(tileEntity, mode, info);
				}
			} else if(state.getBlock() instanceof BlockBait) {
				BaitTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), BaitTileEntity.class);
				if(tileEntity != null) {
					addBaitInfo(tileEntity, mode, info);
				}
			} else if(state.getBlock() instanceof WoodenCrucibleBlock) {
				WoodenCrucibleTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), WoodenCrucibleTileEntity.class);
				if(tileEntity != null) {
					addWoodenCrucibleInfo(tileEntity, mode, info);
				}
			} else if(state.getBlock() instanceof HeavySieveBlock) {
				HeavySieveTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), HeavySieveTileEntity.class);
				if(tileEntity != null) {
					addHeavySieveInfo(tileEntity, mode, info);
				}
			}
		}

		private void addAutoSieveInfo(AutoSieveTileEntityBase tileEntity, ProbeMode mode, IProbeInfo info) {
			if(tileEntity.getCustomSkin() != null) {
				info.text(String.format("Skin: %s", tileEntity.getCustomSkin().getName()));
			}
			if(tileEntity.getFoodBoost() > 1f) {
				info.text(String.format("Speed Boost: %.1f", tileEntity.getFoodBoost()));
			}
			if(tileEntity.getEffectiveLuck() > 1f) {
				info.text(String.format("Luck Bonus: %.1f", tileEntity.getEffectiveLuck() - 1));
			}
		}

		private void addBaitInfo(BaitTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
			BaitTileEntity.EnvironmentalCondition environmentalStatus = tileEntity.checkSpawnConditions(true);
			if(environmentalStatus == BaitTileEntity.EnvironmentalCondition.CanSpawn) {
				info.text("You are too close.");
				info.text("The animals are scared away.");
			} else {
				info.text("Unable to lure animals. Right-click for more info.");
				// list.add(TextFormatting.RED + environmentalStatus.langKey); // not doing this until we get localization support
			}
		}

		private void addWoodenCrucibleInfo(WoodenCrucibleTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
			if(tileEntity.getSolidVolume() > 0f) {
				info.text(String.format("Solid Volume: %d", tileEntity.getSolidVolume()));
			}
			if(tileEntity.getFluidTank().getFluidAmount() > 0f) {
				info.text(String.format("Fluid Volume: %d", tileEntity.getFluidTank().getFluidAmount()));
			}
		}

		private void addHeavySieveInfo(HeavySieveTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
			if(tileEntity.getProgress() > 0f) {
				info.progress((int) (tileEntity.getProgress() * 100), 100); // because a simple progress(float) isn't cool enough ..
			}
			ItemStack meshStack = tileEntity.getMeshStack();
			if (!meshStack.isEmpty()) {
				if(ExRegistro.doMeshesHaveDurability()) {
					info.text(String.format("%s %d/%d", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getItemDamage(), meshStack.getMaxDamage()));
				} else {
					info.text(meshStack.getDisplayName());
				}
			} else {
				info.text("No Mesh");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private static <T extends TileEntity> T tryGetTileEntity(World world, BlockPos pos, Class<T> tileClass) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null && tileClass.isAssignableFrom(tileEntity.getClass())) {
			return (T) tileEntity;
		}
		return null;
	}

}
