package com.recordit.server.dummy;

import java.util.Objects;

public class Foo {
	private String field1;
	private String field2;

	public Foo(String field1, String field2) {
		this.field1 = field1;
		this.field2 = field2;
	}

	public Foo() {
	}

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Foo foo = (Foo)o;
		return Objects.equals(getField1(), foo.getField1()) && Objects.equals(getField2(),
				foo.getField2());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getField1(), getField2());
	}
}
