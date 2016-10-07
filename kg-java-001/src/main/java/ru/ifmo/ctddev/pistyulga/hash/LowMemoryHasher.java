package ru.ifmo.ctddev.pistyulga.hash;

public interface LowMemoryHasher {
	LowMemoryHasher processByte(byte b);
	LowMemoryHasher finish();
	LowMemoryHasher clear();
}
