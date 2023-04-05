package com.tom.chunkstorage.impl;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import com.tom.chunkstorage.ChunkStorage;
import com.tom.chunkstorage.api.ChunkStorageApi;
import com.tom.chunkstorage.api.DataObject;
import com.tom.chunkstorage.api.DataObjectKey;

public class DataStorage {
	private Map<DataObjectKey<?>, DataObject> data;

	public CompoundTag save() {
		if(data == null || data.isEmpty())return null;
		CompoundTag tag = new CompoundTag();
		data.forEach((r, e) -> {
			try {
				tag.put(r.id(), e.save());
			} catch (Exception ex) {
				ChunkStorage.LOGGER.error("Data object failed to save, please report it to the mod author. Object ID: " + r, ex);
			}
		});
		return tag;
	}

	public void load(CompoundTag compound) {
		if(!compound.isEmpty()) {
			data = new HashMap<>();
			compound.getAllKeys().forEach(key -> {
				try {
					ResourceLocation rl = new ResourceLocation(key);
					CompoundTag tag = compound.getCompound(key);
					DataObjectKey<?> k = ChunkStorageApi.getKey(rl);
					if(k == null)k = FallbackObject.key(rl);
					DataObject obj = k.createNew();
					obj.load(tag);
					data.put(k, obj);
				} catch (Exception ex) {
					ChunkStorage.LOGGER.error("Data object failed to load, please report it to the mod author. Object ID: " + key, ex);
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends DataObject> T get(DataObjectKey<T> key) {
		if(data == null)return null;
		DataObject d = data.get(key);
		if(d instanceof FallbackObject)return null;
		return (T) d;
	}

	@SuppressWarnings("unchecked")
	public <T extends DataObject> T getOrCreate(DataObjectKey<T> key) {
		if(data == null)data = new HashMap<>();
		DataObject d = data.computeIfAbsent(key, DataObjectKey::createNew);
		if(d instanceof FallbackObject)throw new IllegalStateException("Attempting to access an unregistered type, please report it to the mod author! Object ID: " + key.id());
		return (T) d;
	}
}
