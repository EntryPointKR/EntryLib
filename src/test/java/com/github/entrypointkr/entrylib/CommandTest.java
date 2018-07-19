package com.github.entrypointkr.entrylib;

import com.github.entrypointkr.entrylib.command.CommandArguments;
import com.github.entrypointkr.entrylib.command.CommandExceptionCatcher;
import com.github.entrypointkr.entrylib.command.ICommand;
import com.github.entrypointkr.entrylib.command.EntryLibCommandArguments;
import com.github.entrypointkr.entrylib.command.CommandSenderEx;
import com.github.entrypointkr.entrylib.command.MapCommand;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class CommandTest {
    static {
        Injector.injectBukkit();
    }

    @Test
    public void execute() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        MapCommand<CommandSenderEx, CommandArguments> command = MapCommand.ofBukkit()
                .put("test", MapCommand.ofBukkit()
                        .put("a", (sender, args) -> {
                            if (!args.isEmpty() && args.get(0).equals("b")) {
                                latch.countDown();
                            }
                        }));
        command.execute(null, EntryLibCommandArguments.of(Arrays.asList("test", "a", "b")));
        Asserts.await(latch);
    }

    @Test
    public void exceptionHandling() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        CommandSenderEx sender = mock(CommandSenderEx.class);
        doAnswer(Asserts.createVoidAnswer(invocation -> latch.countDown()))
                .when(sender).sendMessage(anyString());
        ICommand<CommandSenderEx, CommandArguments> command = CommandExceptionCatcher.ofBukkit(MapCommand.ofBukkit());
        command.execute(sender, EntryLibCommandArguments.of((Collections.singletonList(""))));
        Asserts.await(latch);
    }
}
