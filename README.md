# react-native-gc-list-view
- Fast react native list view
I don't like flatlist which performance is really bad in some old phone. the memory consume is also a issue if item include a lot of picutures. I created the component which is really reusing exist item, not recreate item.

```typescript
<GCListView
    renderItem={this.renderItem}
    data={this.state.data}
    itemLayouts={this.state.itemLayouts}>
</GCListView>
```
```typescript
  renderItem = (item: any, index: number) => {
    return (
      <TouchableOpacity onPress={this.onPress}
        style={{ flex: 1 }}>
        <View style={styles.itemStyle}>
          <Text style={styles.itemTitle}>{item.title}</Text>
          <Image
            style={{ width: 45, height: 45 }}
            source={{ uri: item.url }}></Image>

        </View>
      </TouchableOpacity>);
  }
```
The component need pass itemLayouts for each item. The code mock up to create item layouts
```typescript
  calcItemLayout(data: any[]): number[] {
    data = data || [];
    const itemLayouts: number[] = [];
    let lastOffset = 0;
    let height = 50;
    for (let i = 0; i < data.length; i++) {
      lastOffset += height;
      itemLayouts.push(lastOffset);
    }

    return itemLayouts;
  }
```