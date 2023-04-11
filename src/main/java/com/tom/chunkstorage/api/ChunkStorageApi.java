package com.tom.chunkstorage.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;

import com.tom.chunkstorage.impl.LevelChunkAccess;

/**
 * @author tom5454
 * */
public class ChunkStorageApi {
	public static final String CHUNK_DATA_TAG = "ChunkStorageLib-Data";

	/**
	 * Register a new object type
	 *
	 * @param id The {@link DataObject} type id use modid:type
	 * @param factory The {@link DataObject} factory, used in {@link ChunkStorageApi#getOrCreateFromChunk(LevelChunk, DataObjectKey)}
	 * @return {@link DataObjectKey} The key for the object type
	 * */
	public static synchronized <T extends DataObject> DataObjectKey<T> registerObjectFactory(ResourceLocation id, Supplier<T> factory) {
		if(id == null || factory == null)throw new IllegalArgumentException("Null arguments");
		DataObjectKey<T> key = new DataObjectKey<>(id, factory);
		dataObjectKeys.put(id, key);
		return key;
	}

	/**
	 * Get the stored {@link DataObject} from the chunk.
	 *
	 * @param chunk The Chunk
	 * @param key The {@link DataObjectKey} for the DataObject
	 * @return The {@link DataObject} thats stored in the chunk or null
	 * @throws IllegalAccessException if key or chunk is null
	 * @throws IllegalStateException if chunk is from a client world
	 * */
	public static <T extends DataObject> T getFromChunk(LevelChunk chunk, DataObjectKey<T> key) {
		if(key == null)throw new IllegalArgumentException("Null key");
		if(chunk == null)throw new IllegalArgumentException("Null chunk");
		if(chunk.getLevel().isClientSide)throw new IllegalStateException("Chunk Data accessed from client, Data ID: " + key.id());
		return ((LevelChunkAccess) chunk).csa$getStorage().get(key);
	}

	/**
	 * Get the stored {@link DataObject} from the chunk, if not present create one using the factory.
	 *
	 * @param chunk The Chunk
	 * @param key The {@link DataObjectKey} for the DataObject
	 * @return The {@link DataObject} thats stored in the chunk
	 * @throws IllegalAccessException if key or chunk is null
	 * @throws IllegalStateException if chunk is from a client world
	 * */
	public static <T extends DataObject> T getOrCreateFromChunk(LevelChunk chunk, DataObjectKey<T> key) {
		if(key == null)throw new IllegalArgumentException("Null key");
		if(chunk == null)throw new IllegalArgumentException("Null chunk");
		if(chunk.getLevel().isClientSide)throw new IllegalStateException("Chunk Data accessed from client, Data ID: " + key.id());
		return ((LevelChunkAccess) chunk).csa$getStorage().getOrCreate(key);
	}

	//Internals

	private static Map<ResourceLocation, DataObjectKey<?>> dataObjectKeys = new HashMap<>();

	/**
	 * Internal use only
	 * */
	public static DataObjectKey<?> getKey(ResourceLocation id) {
		return dataObjectKeys.get(id);
	}
}
