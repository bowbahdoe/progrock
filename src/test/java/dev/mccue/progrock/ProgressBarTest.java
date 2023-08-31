package dev.mccue.progrock;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ProgressBarTest {
    @Test
    public void testProgressBar() {
        var bar = new ProgressBar(50);
        assertEquals(bar.total, 50);
        assertEquals(bar.progress, 0);
        assertFalse(bar.isDone);
    }

    @Test
    public void testTick() {
        var bar = new ProgressBar(50);
        assertEquals(bar.tick().progress, 1);
        assertEquals(bar.tick(16).progress, 16);
        assertEquals(bar.tick(5).tick().progress, 6);
    }

    @Test
    public void testDone() {
        var bar = new ProgressBar(50);
        assertTrue(bar.done().isDone);
    }

    @Test
    public void testRender() {
        var bar = new ProgressBar(50);
        assertEquals(
                bar.render(),
                " 0/50     0% [                                                  ]  ETA: --:--"
        );
        assertEquals(
                bar.tick(25).render(),
                "25/50    50% [=========================                         ]  ETA: 00:00"
        );
        assertEquals(
                bar.tick(25).render(RenderOptions.builder()
                        .format("(:bar)")
                        .length(10)
                        .build()
                ),
                "(=====     )"
        );
        assertEquals(
                bar.tick(25).render(RenderOptions.builder()
                        .format("[:bar]")
                        .complete('#')
                        .incomplete('-')
                        .build()
                ),
                "[#########################-------------------------]"
        );

        assertEquals(
                new ProgressBar(0).render(),
                "0/0     0% [                                                  ]  ETA: --:--"
        );
    }

    @Test
    public void testPrint() {
        var bar = new ProgressBar(50);
        var baos = new ByteArrayOutputStream();
        var ps = new PrintStream(baos);

        bar.print(ps);
        assertEquals(
                baos.toString(StandardCharsets.UTF_8),
                "\r 0/50     0% [                                                  ]  ETA: --:--"
        );

        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        bar.print(ps, RenderOptions.builder()
                .length(10)
                .build()
        );
        assertEquals(
                baos.toString(StandardCharsets.UTF_8),
                "\r 0/50     0% [          ]  ETA: --:--"
        );

        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        bar.done().print(ps, RenderOptions.builder()
                .length(10)
                .build()
        );
        assertEquals(
                baos.toString(StandardCharsets.UTF_8),
                "\r 0/50     0% [          ]  ETA: --:--\n"
        );
    }
}
