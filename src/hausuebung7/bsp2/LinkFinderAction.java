/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hausuebung7.bsp2;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

/**
 *
 * @author bmayr
 */
// Recursive Action for forkJoinFramework from Java7
public class LinkFinderAction extends RecursiveAction {

    private String url;
    private ILinkHandler cr;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, ILinkHandler cr) {
        // ToDo: Implement Constructor
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {
        // ToDo:
        // 1. if crawler has not visited url yet:
        // 2. Create new list of recursiveActions
        // 3. Parse url
        // 4. extract all links from url
        // 5. add new Action for each sublink
        // 6. if size of crawler exceeds 500 -> print elapsed time for statistics
        // -> Do not forget to call ìnvokeAll on the actions!

        List<LinkFinderAction> actions = new ArrayList<>();
        if (cr.size() >= 1000) {
            return;
        } else {
            List<String> links = LinkExtractor.extract(url);
            for (String string : links) {
                if (!cr.visited(string) || !((WebCrawler7) cr).getDistinct()) {
                    cr.addVisited(string);
                    //System.out.println(string);
                    LinkFinderAction lfa = new LinkFinderAction(string, cr);
                    actions.add(lfa);
                }
            }
            if (cr instanceof WebCrawler7) {
                ForkJoinPool pool = ((WebCrawler7) cr).getPool();
                for (LinkFinderAction action : actions) {
                    pool.invoke(action);

                }
            }
        }
    }

}
