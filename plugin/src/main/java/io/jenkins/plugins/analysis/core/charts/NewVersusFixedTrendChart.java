package io.jenkins.plugins.analysis.core.charts;

import edu.hm.hafner.echarts.BuildResult;
import edu.hm.hafner.echarts.ChartModelConfiguration;
import edu.hm.hafner.echarts.LineSeries;
import edu.hm.hafner.echarts.LineSeries.FilledMode;
import edu.hm.hafner.echarts.LineSeries.StackedMode;
import edu.hm.hafner.echarts.LinesChartModel;
import edu.hm.hafner.echarts.LinesDataSet;

import io.jenkins.plugins.analysis.core.util.AnalysisBuildResult;
import io.jenkins.plugins.echarts.JenkinsPalette;

/**
 * Builds the model for a trend chart showing all new and fixed issues for a given number of builds.
 *
 * @author Ullrich Hafner
 */
public class NewVersusFixedTrendChart implements TrendChart {
    @Override
    public LinesChartModel create(final Iterable<? extends BuildResult<AnalysisBuildResult>> results,
            final ChartModelConfiguration configuration) {
        NewVersusFixedSeriesBuilder builder = new NewVersusFixedSeriesBuilder();
        LinesDataSet dataSet = builder.createDataSet(configuration, results);

        LinesChartModel model = new LinesChartModel();
        model.setDomainAxisLabels(dataSet.getDomainAxisLabels());

        LineSeries newSeries = getSeries(dataSet, Messages.New_Warnings_Short(), JenkinsPalette.DATA_ORANGE_LIGHT,
                NewVersusFixedSeriesBuilder.NEW);
        LineSeries fixedSeries = getSeries(dataSet, Messages.Fixed_Warnings_Short(), JenkinsPalette.DATA_GREEN_LIGHT,
                NewVersusFixedSeriesBuilder.FIXED);

        model.addSeries(newSeries, fixedSeries);

        return model;
    }

    private LineSeries getSeries(final LinesDataSet dataSet,
            final String name, final JenkinsPalette color, final String dataSetId) {
        LineSeries newSeries = new LineSeries(name, color.getColor(), StackedMode.SEPARATE_LINES, FilledMode.FILLED);
        newSeries.addAll(dataSet.getSeries(dataSetId));
        return newSeries;
    }
}
