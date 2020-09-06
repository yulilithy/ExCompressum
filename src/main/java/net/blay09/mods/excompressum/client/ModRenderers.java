package net.blay09.mods.excompressum.client;

import net.blay09.mods.excompressum.client.render.entity.RenderAngryChicken;
import net.blay09.mods.excompressum.client.render.tile.*;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.tile.ModTileEntities;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModRenderers {

    public void register() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.heavySieve, RenderHeavySieve::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoSieve, RenderAutoSieve::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.manaSieve, RenderAutoSieve::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoHeavySieve, RenderAutoSieve::heavy);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoHammer, RenderAutoHammer::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoCompressedHammer, RenderAutoHammer::compressed);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.woodenCrucible, RenderWoodenCrucible::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.bait, RenderBait::new);

        final ChickenModel<AngryChickenEntity> modelChicken = new ChickenModel<>();
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.angryChicken, manager -> new RenderAngryChicken(manager, modelChicken, 0.3f));
    }

}
