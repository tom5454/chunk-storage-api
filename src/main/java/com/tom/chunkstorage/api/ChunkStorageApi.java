package com.tom.chunkstorage.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;

import com.tom.chunkstorage.impl.LevelChunkAccess;

public class ChunkStorageApi {
	public static final String CHUNK_DATA_TAG = "ChunkStorageLib-Data";

	public static synchronized <T extends DataObject> DataObjectKey<T> registerObjectFactory(ResourceLocation id, Supplier<T> factory) {
		if(id == null || factory == null)throw new IllegalArgumentException("Null arguments");
		DataObjectKey<T> key = new DataObjectKey<>(id, factory);
		dataObjectKeys.put(id, key);
		return key;
	}

	public static <T extends DataObject> T getFromChunk(LevelChunk chunk, DataObjectKey<T> key) {
		if(key == null)throw new IllegalArgumentException("Null key");
		return ((LevelChunkAccess) chunk).csa$getStorage().get(key);
	}

	public static <T extends DataObject> T getOrCreateFromChunk(LevelChunk chunk, DataObjectKey<T> key) {
		if(key == null)throw new IllegalArgumentException("Null key");
		return ((LevelChunkAccess) chunk).csa$getStorage().getOrCreate(key);
	}

	//Internals

	private static Map<ResourceLocation, DataObjectKey<?>> dataObjectKeys = new HashMap<>();

	public static DataObjectKey<?> getKey(ResourceLocation id) {
		return dataObjectKeys.get(id);
	}
}
