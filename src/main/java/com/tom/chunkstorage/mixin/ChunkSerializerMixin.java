package com.tom.chunkstorage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.lighting.LevelLightEngine;

import com.mojang.serialization.Codec;

import com.tom.chunkstorage.api.ChunkStorageApi;
import com.tom.chunkstorage.impl.LevelChunkAccess;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;setLightCorrect(Z)V"), method = "read", locals = LocalCapture.CAPTURE_FAILHARD)
	private static void onRead(ServerLevel serverLevel, PoiManager poiManager, ChunkPos chunkPos, CompoundTag compoundTag, CallbackInfoReturnable<ProtoChunk> cbi, ChunkPos chunkPos2, UpgradeData upgradeData, boolean bl, ListTag listTag, int i, LevelChunkSection levelChunkSections[], boolean bl2, ChunkSource chunkSource, LevelLightEngine levelLightEngine, Registry<Biome> registry, Codec<PalettedContainerRO<Holder<Biome>>> codec, boolean bl3, long m, ChunkStatus.ChunkType chunkType, BlendingData blendingData, ChunkAccess chunkAccess) {
		csa$onRead0(chunkAccess, compoundTag);
	}

	@Surrogate
	private static void onRead(ServerLevel serverLevel, PoiManager poiManager, ChunkPos chunkPos, CompoundTag compoundTag, CallbackInfoReturnable<ProtoChunk> cbi, UpgradeData upgradeData, boolean bl, ListTag listTag, int i, LevelChunkSection levelChunkSections[], boolean bl2, ChunkSource chunkSource, LevelLightEngine levelLightEngine, Registry<Biome> registry, long m, ChunkStatus.ChunkType chunkType, BlendingData blendingData, ChunkAccess chunkAccess) {
		csa$onRead0(chunkAccess, compoundTag);
	}

	private static void csa$onRead0(ChunkAccess chunkAccess, CompoundTag compoundTag) {
		if(chunkAccess instanceof LevelChunkAccess lc && compoundTag.contains(ChunkStorageApi.CHUNK_DATA_TAG)) {
			lc.csa$getStorage().load(compoundTag.getCompound(ChunkStorageApi.CHUNK_DATA_TAG));
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/storage/ChunkSerializer;saveTicks(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/chunk/ChunkAccess$TicksToSave;)V"), method = "write", locals = LocalCapture.CAPTURE_FAILHARD)
	private static void onWrite(ServerLevel serverLevel, ChunkAccess chunkAccess, CallbackInfoReturnable<CompoundTag> cbi, ChunkPos chunkPos, CompoundTag compoundTag) {
		if(chunkAccess.getStatus().getChunkType() != ChunkStatus.ChunkType.PROTOCHUNK) {
			LevelChunkAccess lc = (LevelChunkAccess) chunkAccess;
			CompoundTag tag = lc.csa$getStorage().save();
			if(tag != null)
				compoundTag.put(ChunkStorageApi.CHUNK_DATA_TAG, tag);
		}
	}
}
