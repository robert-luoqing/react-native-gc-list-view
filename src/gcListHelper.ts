import { GCSingleBindView } from "./gcSingleBindView";

/**
 * The class is used to coordinate arrange item
 * We have two method to arrange item.
 * One is arrange item in server side.
 * One is arrange item in client side.
 * The class is used to arrange items in client side
 */
export class GCListHelper {
    subviews:GCSingleBindView[] = [];
    
    registerSubview(view: GCSingleBindView) {
        this.subviews.push(view);
    }
    
    handleScrollChange() {

    }
}