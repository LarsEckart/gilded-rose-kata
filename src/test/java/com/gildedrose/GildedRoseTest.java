package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void normalItems_degradeInQuality_beforeAndAfterSellByDate() {
        // Normal item before sell-by date: quality decreases by 1
        Item[] items = new Item[] {
            new Item("Normal Item", 10, 20),
            new Item("Another Item", 5, 10),
            new Item("Expired Item", 0, 10)  // On sell-by date
        };
        GildedRose app = new GildedRose(items);
        
        app.updateQuality();
        
        // Day 1: quality -1, sellIn -1
        assertEquals(19, items[0].quality);
        assertEquals(9, items[0].sellIn);
        
        assertEquals(9, items[1].quality);
        assertEquals(4, items[1].sellIn);
        
        // After sell-by date (sellIn becomes -1): quality degrades twice as fast
        assertEquals(8, items[2].quality);  // -2 because sellIn < 0
        assertEquals(-1, items[2].sellIn);
        
        // Run another day on the expired item
        app.updateQuality();
        assertEquals(6, items[2].quality);  // -2 again
        assertEquals(-2, items[2].sellIn);
    }

    @Test
    void specialItems_agedBrieIncreasesInQuality_backstagePassesIncreaseByDifferentRates() {
        Item[] items = new Item[] {
            new Item("Aged Brie", 10, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),  // >10 days
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20),  // =10 days
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20),   // =5 days
            new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)    // Concert day
        };
        GildedRose app = new GildedRose(items);
        
        app.updateQuality();
        
        // Aged Brie increases by 1
        assertEquals(21, items[0].quality);
        assertEquals(9, items[0].sellIn);
        
        // Backstage pass >10 days: +1
        assertEquals(21, items[1].quality);
        assertEquals(14, items[1].sellIn);
        
        // Backstage pass at 10 days: +2 (10 days or less)
        assertEquals(22, items[2].quality);
        assertEquals(9, items[2].sellIn);
        
        // Backstage pass at 5 days: +3 (5 days or less)
        assertEquals(23, items[3].quality);
        assertEquals(4, items[3].sellIn);
        
        // Backstage pass after concert: drops to 0
        assertEquals(0, items[4].quality);
        assertEquals(-1, items[4].sellIn);
    }

    @Test
    void qualityConstraints_neverNegative_neverAbove50_exceptSulfuras() {
        Item[] items = new Item[] {
            new Item("Normal Item", 5, 0),      // Quality can't go negative
            new Item("Aged Brie", 5, 50),       // Quality can't exceed 50
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 50),  // Quality can't exceed 50
            new Item("Sulfuras, Hand of Ragnaros", 10, 80)  // Legendary: never changes
        };
        GildedRose app = new GildedRose(items);
        
        app.updateQuality();
        
        // Normal item quality stays at 0 (doesn't go negative)
        assertEquals(0, items[0].quality);
        assertEquals(4, items[0].sellIn);
        
        // Aged Brie quality stays at 50 (doesn't exceed 50)
        assertEquals(50, items[1].quality);
        assertEquals(4, items[1].sellIn);
        
        // Backstage pass quality stays at 50 (doesn't exceed 50)
        assertEquals(50, items[2].quality);
        assertEquals(4, items[2].sellIn);
        
        // Sulfuras never changes (quality or sellIn)
        assertEquals(80, items[3].quality);
        assertEquals(10, items[3].sellIn);
    }
}
