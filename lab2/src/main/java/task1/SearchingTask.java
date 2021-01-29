package task1;

class SearchingTask {

    int xStart, xEnd;
    int yStart, yEnd;

    public SearchingTask(int xStart, int xEnd, int yStart, int yEnd) {
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
    }

    public int getxStart() {
        return xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public int getyEnd() {
        return yEnd;
    }
}
