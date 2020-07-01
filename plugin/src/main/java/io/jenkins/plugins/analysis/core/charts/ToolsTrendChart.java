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
 * Builds the line model for a trend chart showing the total number of issues per tool for a given number of builds.
 *
 * @author Ullrich Hafner
 */
public class ToolsTrendChart implements TrendChart {
    @Override
    public LinesChartModel create(final Iterable<? extends BuildResult<AnalysisBuildResult>> results,
            final ChartModelConfiguration configuration) {
        ToolSeriesBuilder builder = new ToolSeriesBuilder();
        LinesDataSet lineModel = builder.createDataSet(configuration, results);

        LinesChartModel model = new LinesChartModel();

        JenkinsPalette[] colors = {JenkinsPalette.DATA_AQUA_DARK, JenkinsPalette.DATA_GREEN_DARK, JenkinsPalette.DATA_ORANGE_DARK,
        JenkinsPalette.DATA_YELLOW_DARK};
        int index = 0;
        for (String name : lineModel.getDataSetIds()) {
            LineSeries lineSeries = new LineSeries(name, colors[index++].getColor(),
                    StackedMode.SEPARATE_LINES, FilledMode.LINES);
            if (index == colors.length) {
                index = 0;
            }
            lineSeries.addAll(lineModel.getSeries(name));
            model.addSeries(lineSeries);
        }

        model.setDomainAxisLabels(lineModel.getDomainAxisLabels());

        return model;
    }
}
