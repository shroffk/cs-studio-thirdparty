/*
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.archive.common.service.sqlimpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.csstudio.archive.common.service.channel.ArchiveChannelDTO;
import org.csstudio.archive.common.service.channel.ArchiveChannelId;
import org.csstudio.archive.common.service.channel.IArchiveChannel;
import org.csstudio.archive.common.service.channelgroup.ArchiveChannelGroupId;
import org.csstudio.archive.common.service.channelgroup.IArchiveChannelGroup;
import org.csstudio.archive.common.service.engine.ArchiveEngineId;
import org.csstudio.archive.common.service.engine.IArchiveEngine;
import org.csstudio.archive.common.service.samplemode.ArchiveSampleMode;
import org.csstudio.archive.common.service.samplemode.ArchiveSampleModeId;
import org.csstudio.archive.common.service.samplemode.IArchiveSampleMode;
import org.csstudio.archive.rdb.ChannelConfig;
import org.csstudio.archive.rdb.SampleMode;
import org.csstudio.archive.rdb.engineconfig.ChannelGroupConfig;
import org.csstudio.archive.rdb.engineconfig.SampleEngineConfig;
import org.csstudio.domain.desy.epics.types.EpicsIValueTypeSupport;
import org.csstudio.domain.desy.types.BaseTypeConversionSupport;
import org.csstudio.platform.data.ITimestamp;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * This adapter translates the originally used types in the archive.rdb to new interface types.
 *
 * These new types shall decouple the two layers AND be as slim as possible, so that data that is
 * not used by the client (engine or writer) is not present in the client.
 *
 * @author bknerr
 * @since 12.11.2010
 */
public enum ArchiveEngineAdapter {

    INSTANCE;

    /**
     * @author bknerr
     * @since 22.12.2010
     */
    private static final class ArchiveChannel2ChannelCfgFunction implements
            Function<ChannelGroupConfig, IArchiveChannelGroup> {
        /**
         * Constructor.
         */
        public ArchiveChannel2ChannelCfgFunction() {
            // Empty
        }
        @Override
        @Nonnull
        public IArchiveChannelGroup apply(final ChannelGroupConfig from) {
            return adapt(from);
        }
    }
    private static final ArchiveChannel2ChannelCfgFunction ARCH_CHAN_2_CHAN_CFG_FUNC =
        new ArchiveChannel2ChannelCfgFunction();


    /**
     * Constructor.
     */
    private ArchiveEngineAdapter() {
        EpicsIValueTypeSupport.install();
    }

    /**
     * @param cfg the archive.rdb type for sample engine config
     * @return the service interface type for the engine
     */
    public IArchiveEngine adapt(@Nonnull final SampleEngineConfig cfg) {

        return new IArchiveEngine() {

            @Override
            @Nonnull
            public ArchiveEngineId getId() {
                return new ArchiveEngineId(cfg.getId());
            }

            @Override
            @CheckForNull
            public URL getUrl() throws MalformedURLException {
                try {
                    return cfg.getUrl();
                } catch (final Exception e) {
                    // FIXME (kasemir) : untyped exception swallows anything, use dedicated exception
                    throw new MalformedURLException();
                }
            }

        };
    }

    /**
     * @param channel the archive.rdb channel configuration
     * @return the service interface for the channel configuration
     */
    @Nonnull
    public IArchiveChannel adapt(@Nonnull final ChannelConfig channel,
                                 @Nullable final ITimestamp lastTimestamp) {

        final IArchiveChannel cfg = new ArchiveChannelDTO(new ArchiveChannelId(channel.getId()),
                                                          channel.getName(),
                                                          "<not set>",
                                                          new ArchiveChannelGroupId(channel.getGroupId()),
                                                          new ArchiveSampleModeId(channel.getSampleMode().getId()),
                                                          channel.getSamplePeriod(),
                                                          BaseTypeConversionSupport.toTimeInstant(lastTimestamp),
                                                          channel.getSampleValue());

        return cfg;
    }

    /**
     * @param cfg cfg the archive.rdb channel group config
     * @return the service interface for this config
     */
    @Nonnull
    public static IArchiveChannelGroup adapt(@Nonnull final ChannelGroupConfig cfg) {
        return new IArchiveChannelGroup() {
            @Override
            @Nonnull
            public ArchiveChannelGroupId getId() {
                return new ArchiveChannelGroupId(cfg.getId());
            }
            @Override
            @CheckForNull
            public String getName() {
                return cfg.getName();
            }
            @Override
            @CheckForNull
            public ArchiveChannelId getEnablingChannelId() {
                return new ArchiveChannelId(cfg.getId());
            }
        };
    }

    /**
     * @param groups the list of archive.rdb group configurations
     * @return the list of service interfaces for the group configs
     */
    @Nonnull
    public static List<IArchiveChannelGroup> adapt(@CheckForNull final ChannelGroupConfig[] groups) {
        if (groups == null) {
            return Collections.emptyList();
        }
        final List<ChannelGroupConfig> list = Lists.newArrayList(groups);

        return Lists.transform(list, ARCH_CHAN_2_CHAN_CFG_FUNC);
    }



    /**
     * @param sampleMode
     * @return
     */
    @CheckForNull
    public IArchiveSampleMode adapt(@Nonnull final SampleMode sampleMode) {
        for (final ArchiveSampleMode mode : ArchiveSampleMode.values()) {
            if (sampleMode.getName().toUpperCase().equals(mode.name())) {
                return mode;
            }
        }
        return null;
    }
}
