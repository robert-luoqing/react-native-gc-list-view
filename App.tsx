/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, { useState } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  Image,
  TouchableOpacity
} from 'react-native';

import GCCoordinatorView from './src/gcCoordinatorView';
// import { GCSingleBindView } from './src/gcSingleBindView';
import { GCListView } from './src/gcListView';
import GCCustomScrollView from './src/gcCustomScrollView';

class App extends React.PureComponent<any, any> {
  uris = [
    "http://e.hiphotos.baidu.com/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
    "http://b.hiphotos.baidu.com/image/h%3D300/sign=ad628627aacc7cd9e52d32d909032104/32fa828ba61ea8d3fcd2e9ce9e0a304e241f5803.jpg",
    "http://g.hiphotos.baidu.com/image/h%3D300/sign=b5e4c905865494ee982209191df4e0e1/c2cec3fdfc03924590b2a9b58d94a4c27d1e2500.jpg",
    "http://f.hiphotos.baidu.com/image/h%3D300/sign=0c78105b888ba61ec0eece2f713597cc/0e2442a7d933c8956c0e8eeadb1373f08202002a.jpg",
    "http://a.hiphotos.baidu.com/image/h%3D300/sign=b38f3fc35b0fd9f9bf175369152cd42b/9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg",
    "http://b.hiphotos.baidu.com/image/h%3D300/sign=05b297ad39fa828bce239be3cd1e41cd/0eb30f2442a7d9337119f7dba74bd11372f001e0.jpg",
    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2559612600,3506634088&fm=26&gp=0.jpg",
    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3791586521,940241723&fm=26&gp=0.jpg",
    "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3613272,3805344273&fm=26&gp=0.jpg",
    "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3022319459,3364132540&fm=26&gp=0.jpg"
  ];

  constructor(props: any) {
    super(props);
    const data = [];
    for (let i = 0; i < 100000; i++) {
      data.push({
        title: "The data is: " + i,
        url: this.uris[i % 10]
      });
    }

    this.state = {
      data: data,
      itemLayouts: this.calcItemLayout(data, true)
    }

    // setTimeout(() => {
    //   const newData = [];
    //   for (let i = 0; i < 10; i++) {
    //     newData.push({
    //       title: "The new data is: " + i,
    //       url: this.uris[i % 10]
    //     });
    //   }

    //   this.setState({
    //     data: newData,
    //     itemLayouts: this.calcItemLayout(newData)
    //   });
    // }, 10000);
  }

  calcItemLayout(data: any[], regularHeight: boolean): number[] {
    data = data || [];
    const itemLayouts: number[] = [];
    let lastOffset = 0;
    let height = 50;
    for (let i = 0; i < data.length; i++) {
      lastOffset += height;
      if (!regularHeight) {
        if (height > 120) {
          height = 50
        } else {
          height = height + 10;
        }
      }

      itemLayouts.push(lastOffset);
    }

    return itemLayouts;
  }

  render() {
    return (
      <>
        <StatusBar barStyle="dark-content" />
        <SafeAreaView>
          <GCListView
            renderItem={this.renderItem}
            data={this.state.data}
            itemLayouts={this.state.itemLayouts}>
          </GCListView>
        </SafeAreaView>
      </>
    );
  }

  renderItem = (item: any, index: number) => {
    return (
      <TouchableOpacity onPress={this.onPress}
        style={{ flex: 1 }}>
        <View style={styles.itemStyle}>
          <Text style={styles.itemTitle}>{item.title}</Text>
          <Text style={styles.itemTitle}>{item.title}</Text>
          <Image
            style={{ width: 45, height: 45 }}
            source={{ uri: item.url }}></Image>
          <Image
            style={{ width: 45, height: 45 }}
            source={{ uri: item.url }}></Image>

        </View>
      </TouchableOpacity>);
  }

  onPress = () => {

  }
};

const styles = StyleSheet.create({
  itemStyle: {
    width: "100%",
    height: "100%",
    backgroundColor: "yellow",
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingLeft: 10,
    paddingRight: 10,
    paddingTop: 2,
    paddingBottom: 2,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: "#ddd",
  },
  itemTitle: {
    fontSize: 16,
    fontWeight: "700",
    lineHeight: 48

  }
});

export default App;
