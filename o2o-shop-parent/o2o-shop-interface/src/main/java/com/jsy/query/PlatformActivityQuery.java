
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;

/**
 *
 * @author lijin
 * @since 2020-11-20
 */
public class PlatformActivityQuery<T> extends BaseQuery{

    private T query;

    public PlatformActivityQuery() {
    }

    public T getQuery() {
        return this.query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlatformActivityQuery)) return false;
        final PlatformActivityQuery<?> other = (PlatformActivityQuery<?>) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$query = this.getQuery();
        final Object other$query = other.getQuery();
        if (this$query == null ? other$query != null : !this$query.equals(other$query)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlatformActivityQuery;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $query = this.getQuery();
        result = result * PRIME + ($query == null ? 43 : $query.hashCode());
        return result;
    }

    public String toString() {
        return "PlatformActivityQuery(query=" + this.getQuery() + ")";
    }
}