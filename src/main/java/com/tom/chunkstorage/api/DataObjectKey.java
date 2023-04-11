package com.tom.chunkstorage.api;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;

/**
 * @author tom5454
 * */
public class DataObjectKey<T extends DataObject> {
	private final ResourceLocation id;
	private final int hash;
	private final Supplier<T> factory;

	/**
	 * Internal use only
	 *
	 * Use {@link ChunkStorageApi#registerObjectFactory(ResourceLocation, Supplier)}
	 * */
	public DataObjectKey(ResourceLocation id, Supplier<T> factory) {
		this.id = id;
		this.hash = id.hashCode();
		this.factory = factory;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DataObjectKey<?> other = (DataObjectKey<?>) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	public String id() {
		return id.toString();
	}

	public T createNew() {
		return factory.get();
	}
}
