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

export interface GCRelevantData {
    key: string;
    offset: number;
    category?: string;
}

interface GCListViewPropInner {
    data: any[];

    /**
     * The offset will be set
     * if there are three elements like:
     * element 1: offset: 0, height: 10,
     * element 1: offset: 10, height: 20,
     * element 1: offset: 30, height: 20,
     * The array should be [10,30,50]
     */
    relevantData: GCRelevantData[];
    preloadFrame?: number;
    invert?: boolean;
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
            bindList
        };
    }

    UNSAFE_componentWillReceiveProps(nextProps: any) {
        if (nextProps.relevantData !== this.props.relevantData) {
            this.calcItemLayout(nextProps.relevantData);
        }
    }

    calcItemLayout(relevantData: GCRelevantData[]) {
        this.scrollContentHeight = 0;
        if (relevantData && relevantData.length > 0) {
            const obj = relevantData[relevantData.length - 1];
            this.scrollContentHeight = obj.offset;
        }
    }

    componentDidMount() {
        this.calcItemLayout(this.props.relevantData);
        setTimeout(() => {
            this.setState({
                forceIndex: this.state.forceIndex + 1
            });
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
                data={this.props.relevantData || []}
                preloadFrame={this.props.preloadFrame || 1}
                invert={this.props.invert || false}
                style={{
                    height: this.scrollContentHeight,
                }}
            >
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
        </ScrollView>;
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
