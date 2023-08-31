package dev.mccue.progrock;

import java.util.Objects;

/**
 * Options to be applied when a {@link ProgressBar} is rendered as
 * text.
 */
public final class RenderOptions {
    public static final RenderOptions DEFAULT = new RenderOptions(
            50,
            ":progress/:total   :percent% [:bar]  ETA: :remaining",
            '=',
            ' '
    );

    final int length;
    final String format;
    final char complete;
    final char incomplete;

    private RenderOptions(
            int length,
            String format,
            char complete,
            char incomplete
    ) {
        this.length = length;
        this.format = format;
        this.complete = complete;
        this.incomplete = incomplete;
    }

    private RenderOptions(
            Builder builder
    ) {
        this.length = Objects.requireNonNullElse(builder.length, DEFAULT.length);
        this.format = Objects.requireNonNullElse(builder.format, DEFAULT.format);
        this.complete = Objects.requireNonNullElse(builder.complete, DEFAULT.complete);
        this.incomplete = Objects.requireNonNullElse(builder.incomplete, DEFAULT.incomplete);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RenderOptions other &&
                this.length == other.length &&
                this.format.equals(other.format) &&
                this.complete == other.complete &&
                this.incomplete == other.incomplete;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, format, complete, incomplete);
    }

    @Override
    public String toString() {
        return "RenderOptions[" +
                "length=" + length +
                ", format='" + format + '\'' +
                ", complete=" + complete +
                ", incomplete=" + incomplete +
                ']';
    }

    /**
     * Creates a new {@link Builder}.
     * @return A new {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new {@link Builder}, copying values from an existing {@link RenderOptions}.
     * @return A new {@link Builder}.
     */
    public static Builder builder(RenderOptions renderOptions) {
        return new Builder(renderOptions);
    }

    /**
     * A builder which constructs instances of {@link RenderOptions}.
     */
    public static final class Builder {
        Integer length = null;
        String format = null;
        Character complete = null;
        Character incomplete = null;

        private Builder() {}

        private Builder(RenderOptions renderOptions) {
            length = renderOptions.length;
            format = renderOptions.format;
            complete = renderOptions.complete;
            incomplete = renderOptions.incomplete;
        }

        /**
         * Sets the length of the bar.
         * @param length The length of the bar.
         * @return The updated {@link Builder}.
         */
        public Builder length(int length) {
            this.length = length;
            return this;
        }

        /**
         * Sets the format string for the {@link Builder}.
         *
         * <p>
         * The format string determines how the bar is displayed, with the following
         * substitutions:
         * </p>
         *
         * <ul>
         *     <li>:bar       - the progress bar itself</li>
         *     <li>:progress  - the number of complete items</li>
         *     <li>:total     - the total number of items</li>
         *     <li>:percent   - the percentage done</li>
         *     <li>:elapsed   - the elapsed time in minutes and seconds</li>
         *     <li>:remaining - the estimated remaining time in minutes and seconds</li>
         * </ul>
         *
         * @param format The format string to use.
         * @return The updated {@link Builder}.
         */
        public Builder format(String format) {
            this.format = format;
            return this;
        }

        /**
         * Sets the character to use for a completed chunk.
         * @param complete The character to use for a completed chunk.
         * @return The updated {@link Builder}.
         */
        public Builder complete(char complete) {
            this.complete = complete;
            return this;
        }

        /**
         * Sets the character to use for an incomplete chunk.
         * @param incomplete The character to use for an incomplete chunk.
         * @return The updated {@link Builder}.
         */
        public Builder incomplete(char incomplete) {
            this.incomplete = incomplete;
            return this;
        }

        /**
         * Constructs a {@link RenderOptions} from the values set in the
         * {@link Builder}.
         *
         * @return A newly constructed {@link RenderOptions}.
         */
        public RenderOptions build() {
            return new RenderOptions(this);
        }
    }
}
