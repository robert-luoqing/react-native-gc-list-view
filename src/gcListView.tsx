import React from 'react';
import {
    StyleSheet,
    ScrollView,
    ScrollViewProps,
    PixelRatio
} from 'react-native';
import GCCoordinatorView from './gcCoordinatorView';
import { GCSingleBindView } from './gcSingleBindView';
import GCScrollItemView from './gcScrollItemView';

interface GCListViewPropInner {
    data: any[];
    /**
     * The control category
     * Try to use same category control to avoid recreate element
     * if the item have difference category
     * If no category item, then null be set.
     */
    categories?: string[];
    /**
     * The offset+height will be set
     * if there are three elements like:
     * element 1: offset: 0, height: 10,
     * element 1: offset: 10, height: 20,
     * element 1: offset: 30, height: 20,
     * The array should be [10,30,50]
     */
    itemLayouts: number[];
    /**
     * The item will be render
     */
    renderItem: (itemData: any, index: number, free?: boolean) => JSX.Element;
}

type GCListViewProp = ScrollViewProps & GCListViewPropInner;

export class GCListView extends React.PureComponent<GCListViewProp, { forceIndex: number; bindList: any[]; }> {
    scrollContentHeight = 0;
    ratio: number;
    constructor(props: any) {
        super(props);
        const bindList = [];

        this.ratio = PixelRatio.get();

        for (let i = 0; i < 100; i++) {
            bindList.push(null);
        }

        this.state = {
            forceIndex: 0,
            bindList: bindList
        }
    }

    UNSAFE_componentWillReceiveProps(nextProps: any) {
        if (nextProps.itemLayouts != this.props.itemLayouts) {
            this.calcItemLayout(nextProps.itemLayouts);
        }
    }

    calcItemLayout(itemLayouts: any[]) {
        this.scrollContentHeight = 0;
        if (itemLayouts && itemLayouts.length > 0) {
            this.scrollContentHeight = itemLayouts[itemLayouts.length - 1];
        }
    }

    componentDidMount() {
        this.calcItemLayout(this.props.itemLayouts);
        setTimeout(() => {
            this.setState({
                forceIndex: this.state.forceIndex + 1
            })
        }, 1);
    }

    render() {
        return <ScrollView
            {...this.props}
            contentInsetAdjustmentBehavior="automatic"
        >
            <GCCoordinatorView
                forceIndex={this.state.forceIndex}
                pixelRatio={this.ratio}
                itemLayouts={this.props.itemLayouts || []}
                categories={this.props.categories || []}
                style={{
                    height: this.scrollContentHeight,
                }}>
                {
                    this.state.bindList.map((_: any, index: any) => {
                        return (
                            <GCScrollItemView
                                key={index}
                                style={styles.itemStyle}>
                                <GCSingleBindView
                                    itemIndex={index}
                                    renderItem={this.props.renderItem}
                                    list={this.props.data}>
                                </GCSingleBindView>
                            </GCScrollItemView>
                        );
                    })
                }
            </GCCoordinatorView>
        </ScrollView>
    }
}

const styles = StyleSheet.create({
    itemStyle: {
        right: 0,
        height: 0,
        position: "absolute",
        left: 0,
        top: -100000,
    }
});