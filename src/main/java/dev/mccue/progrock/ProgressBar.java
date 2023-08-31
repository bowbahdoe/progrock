package dev.mccue.progrock;

import java.io.PrintStream;
import java.util.Objects;

/**
 * An immutable representation of the data needed
 * to create a progress bar for display.
 */
public final class ProgressBar {
    final int progress;
    final int total;
    final boolean isDone;
    final long creationTime;

    private ProgressBar(
            int progress,
            int total,
            boolean isDone,
            long creationTime
    ) {
        this.progress = progress;
        this.total = total;
        this.isDone = isDone;
        this.creationTime = creationTime;
    }

    /**
     * Create a {@link ProgressBar} which goes up to the specified total.
     * @param total The total to go up to.
     */
    public ProgressBar(int total) {
        this(0, total, false, System.currentTimeMillis());
    }

    /**
     * Return a new progress bar that has been incremented by 1.
     *
     * @return A new {@link ProgressBar} incremented by 1.
     */
    public ProgressBar tick() {
        return this.tick(1);
    }

    /**
     * Return a new progress bar that has been incremented by the amount supplied.
     *
     * @return A new {@link ProgressBar} incremented by the amount supplied..
     */
    public ProgressBar tick(int amount) {
        return new ProgressBar(
                Math.max(Math.min(progress + amount, total), 0),
                total,
                isDone,
                creationTime
        );
    }

    /**
     * Returns a new progress bar that is marked as done.
     * @return A new {@link ProgressBar} marked as done.
     */
    public ProgressBar done() {
        return new ProgressBar(
                progress,
                total,
                true,
                creationTime
        );
    }

    private int percent() {
        if (total > 0) {
            return (int) ((progress / (double) total) * 100);
        }
        else {
            return 0;
        }
    }

    private long elapsedTime() {
        return System.currentTimeMillis() - creationTime;
    }

    private String intervalStr(Long milliseconds) {
        if (milliseconds == null) {
            return "--:--";
        }
        else {
            var seconds = (milliseconds / 1000) % 60;
            var minutes = milliseconds / 60000;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private String barText(RenderOptions renderOptions) {
        var progress = this.progress;
        var total = this.total;
        var length = renderOptions.length;
        var complete = renderOptions.complete;
        var incomplete = renderOptions.incomplete;

        var completedRatio = total > 0 ? (progress / (double) total) : 0;
        var completedLength = (int) (completedRatio * length);

        return String.valueOf(complete).repeat(completedLength)
                + String.valueOf(incomplete).repeat(Math.max(length - completedLength, 0));
    }



    private String alignRight(String text, int size) {
        return " ".repeat(size - text.length()) + text;
    }

    private Long remainingTime() {
        var elapsed = elapsedTime();
        if (progress > 0 && total > 0) {
            return (long) ((elapsed / (progress / (double) total)) - elapsed);
        }
        else {
            return null;
        }
    }

    public String render() {
        return render(RenderOptions.DEFAULT);
    }

    public String render(RenderOptions renderOptions) {
        var format = renderOptions.format;
        var bar = barText(renderOptions);
        format = format.replace(":bar", bar);

        var progress = alignRight(
                String.valueOf(this.progress),
                String.valueOf(this.total).length()
        );

        format = format.replace(":progress", progress);

        var total = String.valueOf(this.total);
        format = format.replace(":total", total);

        var percent = alignRight(
                String.valueOf(percent()),
                3
        );
        format = format.replace(":percent", percent);

        var elapsed = intervalStr(elapsedTime());
        format = format.replace(":elapsed", elapsed);

        var remaining = intervalStr(remainingTime());
        format = format.replace(":remaining", remaining);

        return format;
    }

    /**
     * Renders and displays the given progress bar onto a {@link PrintStream}
     * using the provided {@link RenderOptions}.
     * @param printStream The {@link PrintStream} to output to.
     * @param renderOptions The {@link RenderOptions} to use when rendering the {@link ProgressBar}.
     */
    public void print(PrintStream printStream, RenderOptions renderOptions) {
        printStream.print("\r" + render(renderOptions));
        if (isDone) {
            printStream.println();
        }
        printStream.flush();
    }

    /**
     * Renders and displays the given progress bar onto {@link System#out}
     * using {@link RenderOptions#DEFAULT}.
     */
    public void print() {
        print(System.out, RenderOptions.DEFAULT);
    }

    /**
     * Renders and displays the given progress bar onto a {@link PrintStream}
     * using {@link RenderOptions#DEFAULT}.
     * @param printStream The {@link PrintStream} to output to.
     */
    public void print(PrintStream printStream) {
        print(printStream, RenderOptions.DEFAULT);
    }

    /**
     * Renders and displays the given progress bar onto {@link System#out}
     * using the provided {@link RenderOptions}.
     * @param renderOptions The {@link RenderOptions} to use when rendering the {@link ProgressBar}.
     */
    public void print(RenderOptions renderOptions) {
        print(System.out, renderOptions);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ProgressBar other &&
                this.progress == other.progress &&
                this.total == other.total &&
                this.isDone == other.isDone &&
                this.creationTime == other.creationTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(progress, total, isDone, creationTime);
    }

    @Override
    public String toString() {
        return "ProgressBar[" +
                "progress=" + progress +
                ", total=" + total +
                ", isDone=" + isDone +
                ", creationTime=" + creationTime +
                ']';
    }
}
