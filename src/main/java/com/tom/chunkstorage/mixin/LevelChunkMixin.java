package com.tom.chunkstorage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.level.chunk.LevelChunk;

import com.tom.chunkstorage.impl.DataStorage;
import com.tom.chunkstorage.impl.LevelChunkAccess;

@Mixin(LevelChunk.class)
public class LevelChunkMixin implements LevelChunkAccess {
	private @Unique DataStorage csa$storage = new DataStorage();

	@Override
	public DataStorage csa$getStorage() {
		return csa$storage;
	}
}
