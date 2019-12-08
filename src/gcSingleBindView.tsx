import React from 'react';
import {
    View
} from 'react-native';
import GCNotifyView from './gcNotifyView';
import _ from 'lodash';

export class GCSingleBindView extends React.PureComponent<{ list: any[]; renderItem: (itemData: any, index: number) => JSX.Element; itemIndex: number; }, any> {
    private lastData: any;

    constructor(props: any) {
        super(props);
        this.state = {
            data: null,
            height: 0,
            index: -1
        }
    }

    render() {
        return (
            <View style={{
                height: this.state.height
            }}>
                <GCNotifyView
                    onIndexChange={(obj: any) => { this.handleIndexChange(obj.nativeEvent) }}
                />

                {this.state.data &&
                    this.props.renderItem ? this.props.renderItem(this.state.data, this.state.index) : null
                }

            </View>
        );
    }

    handleIndexChange = (obj: any) => {
        const itemData = this.props.list[obj.index];
        const startY = obj.startY;
        const endY = obj.endY;
        this.lastData = {
            data: itemData,
            startY,
            endY,
            index: obj.index
        }

        if (this.state.data) {
            this.debounceDoBind();
        } else {
            this.doBind();
        }
    }

    doBind = () => {
        const bindData = this.lastData;
        const height = bindData.endY - bindData.startY;
        if (this.state.data != bindData.data
            || this.state.height != height
            || this.state.index != bindData.index) {
            this.setState({
                data: bindData.data,
                height: height,
                index: bindData.index
            });
        }
    }

    debounceDoBind = _.debounce(this.doBind, 5);
};